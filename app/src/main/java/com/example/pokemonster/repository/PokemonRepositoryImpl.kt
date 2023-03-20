package com.example.pokemonster.repository

import com.example.pokemonster.io.local.PokemonDatabase
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.io.local.toMoveEntity
import com.example.pokemonster.io.local.toPokemonEntity
import com.example.pokemonster.io.local.toStateEntity
import com.example.pokemonster.io.remote.PokemonAPI
import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.repository.states.Results
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonAPI: PokemonAPI,
    private val pokemonDatabase: PokemonDatabase
) : PokemonRepository {
    override fun getAllPokemon(mutableSharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>) {
        var shouldEmitLoadingStatus = false
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDatabase.pokemonDao().getAllPokemons().collect { pokemons ->
                val hasCachedPokemon = pokemons.isNotEmpty()
                shouldEmitLoadingStatus = !hasCachedPokemon
                if (hasCachedPokemon) {
                    mutableSharedFlow.emit(Results.Success(pokemons))
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            // Get updated pokemon data from the server and cash it to the local data source
            getRemotePokemonAndCache(mutableSharedFlow, shouldEmitLoadingStatus)
        }
    }

    override fun getPokemonById(
        id: Int,
        mutableSharedFlow: MutableSharedFlow<Results<PokemonEntity>>
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            pokemonDatabase.pokemonDao().getPokemonById(id).collect { pokemon ->
                mutableSharedFlow.emit(Results.Success(pokemon))
            }
        }
    }

    override fun searchPokemon(
        searchName: String,
        mutableSharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val pokemons = pokemonDatabase.pokemonDao().searchPokemons("$searchName%")
            mutableSharedFlow.emit(Results.Success(pokemons))
        }
    }

    private fun getRemotePokemonAndCache(
        sharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>,
        shouldEmitLoadingStatus: Boolean
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            if (shouldEmitLoadingStatus) {
                sharedFlow.emit(Results.Loading)
            }
            val pokemonListResponse = pokemonAPI.getPaginatedPokemonList(0, 100) // TODO: use pagination
            if (pokemonListResponse.isSuccessful) {
                val pokemons = pokemonListResponse.body()
                pokemons?.results?.forEach {
                    val urlSplitData = it.url.split('/')
                    val id = urlSplitData[urlSplitData.size - 2].toInt() // TODO: Validate id is correct value and avoid OutOfBoundIndexException
                    val call = async(Dispatchers.IO) {
                        pokemonAPI.getPokemon(id)
                    }
                    try {
                        val response = call.await()
                        if (response.isSuccessful) {
                            withContext(Dispatchers.IO) {
                                response.body()?.let { responseBody ->
                                    pokemonDatabase.pokemonDao().insertPokemon(
                                        responseBody.toPokemonEntity()
                                    )
                                    response.body()?.stats?.forEach { stat ->
                                        pokemonDatabase.pokemonDao().insertPokemonState(
                                            stat.toStateEntity(responseBody.id)
                                        )
                                    }
                                    response.body()?.moves?.forEach { move ->
                                        pokemonDatabase.pokemonDao().insertPokemonMove(
                                            move.toMoveEntity(responseBody.id)
                                        )
                                    }
                                }
                            }
                        } else {
                            sharedFlow.emit(Results.OnError(Exception(response.message())))
                        }
                    } catch (e: Throwable) {
                        sharedFlow.emit(Results.OnError(Exception(e.localizedMessage)))
                    }
                }
            }
            pokemonDatabase.pokemonDao().getAllPokemons().collect { pokemons ->
                sharedFlow.emit(Results.Success(pokemons))
            }
        }
    }

    override fun getMoveDetails(
        id: Int,
        mutableSharedFlow: MutableSharedFlow<Results<MoveResponse>>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            mutableSharedFlow.emit(Results.Loading)
            try {
                val response = pokemonAPI.getMoveDetails(id)
                if (response.isSuccessful && response.body() != null) {
                    mutableSharedFlow.emit(Results.Success(response.body()!!))
                } else {
                    mutableSharedFlow.emit(Results.OnError(Exception(response.message())))
                }
            } catch (e: Throwable) {
                mutableSharedFlow.emit(Results.OnError(Exception(e.localizedMessage)))
            }
        }
    }

    override suspend fun getPokemonStates(
        pokemonId: Int
    ): List<PokemonStatEntity> {
        val call = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonStates(pokemonId)
        }
        return call.await()
    }

    override suspend fun getPokemonMoves(
        pokemonId: Int
    ): List<PokemonMoveEntity> {
        val call = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonMoves(pokemonId)
        }
        return call.await()
    }

    override suspend fun getAllFavoritePokemon(
        mutableSharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val pokemons = pokemonDatabase.pokemonDao().getFavoritePokemon()
            mutableSharedFlow.emit(Results.Success(pokemons))
        }
    }

    override fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDatabase.pokemonDao().setMoveEffectDescription(pokemonMoveEntity)
        }
    }

    override fun updatePokemon(pokemonEntity: PokemonEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDatabase.pokemonDao().updatePokemon(pokemonEntity)
        }
    }
}