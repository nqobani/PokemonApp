package com.example.pokemonster.repository

import com.example.pokemonster.io.local.LocalDataSource
import com.example.pokemonster.io.remote.RemoteDatasource
import com.example.pokemonster.io.remote.models.moves.MoveRemoteResponse
import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.model.PokemonMove
import com.example.pokemonster.model.PokemonStats
import com.example.pokemonster.model.toProjectPokemon
import com.example.pokemonster.model.toProjectPokemonMove
import com.example.pokemonster.model.toProjectPokemonStat
import com.example.pokemonster.repository.states.Results
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

class PokemonRepository @Inject constructor(
    private val remoteDatasource: RemoteDatasource,
    private val localDataSource: LocalDataSource
) {
    fun getAllPokemon(mutableSharedFlow: MutableSharedFlow<Results<List<Pokemon>>>) {
        var shouldEmitLoadingStatus = false
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.getAllPokemon().collect { pokemons ->
                val hasCachedPokemon = pokemons.isNotEmpty()
                shouldEmitLoadingStatus = !hasCachedPokemon
                if (hasCachedPokemon) {
                    mutableSharedFlow.emit(Results(Results.Status.SUCCESS, pokemons, null))
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            // Get updated pokemon data from the server and cash it to the local data source
            getRemotePokemonAndCache(mutableSharedFlow, shouldEmitLoadingStatus)
        }
    }

    private fun getRemotePokemonAndCache(
        sharedFlow: MutableSharedFlow<Results<List<Pokemon>>>,
        shouldEmitLoadingStatus: Boolean
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            if (shouldEmitLoadingStatus) {
                sharedFlow.emit(Results(Results.Status.LOADING, null, null))
            }
            val result = remoteDatasource.getPaginatedPokemonList(0, 100) // TODO: use pagination
            when (result.status) {
                Results.Status.SUCCESS -> {
                    val pokemonList = result.data
                    pokemonList?.results?.forEach {
                        val urlSplitData = it.url.split('/')
                        val id = urlSplitData[urlSplitData.size - 2].toInt() // TODO: Validate id is correct value and avoid OutOfBoundIndexException
                        val pokemonDetails = remoteDatasource.getPokemon(id)
                        when (pokemonDetails.status) {
                            Results.Status.SUCCESS -> {
                                val details = pokemonDetails.data
                                withContext(Dispatchers.IO) {
                                    details?.let { responseBody ->
                                        localDataSource.addPokemon(
                                            responseBody.toProjectPokemon()
                                        )
                                        details.stats.forEach { stat ->
                                            localDataSource.addStat(
                                                stat.toProjectPokemonStat(responseBody.id)
                                            )
                                        }
                                        details.moves.forEach { move ->
                                            localDataSource.addMove(
                                                move.toProjectPokemonMove(responseBody.id)
                                            )
                                        }
                                    }
                                }
                            }
                            Results.Status.LOADING -> {
                                // Do nothing it is already handled
                            }
                            Results.Status.ERROR -> {
                                sharedFlow.emit(
                                    Results(Results.Status.ERROR, null, pokemonDetails.error)
                                )
                            }
                        }
                    }
                }
                Results.Status.LOADING -> {
                    // Do nothing it is already handled
                }
                Results.Status.ERROR -> {
                    sharedFlow.emit(Results(Results.Status.ERROR, null, result.error))
                }
            }
        }
    }

    fun getMoveDetails(
        id: Int,
        mutableSharedFlow: MutableSharedFlow<Results<MoveRemoteResponse>>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            mutableSharedFlow.emit(Results(Results.Status.LOADING, null, null))
            try {
                val result = remoteDatasource.getMoveDetails(id)
                when (result.status) {
                    Results.Status.SUCCESS -> {
                        mutableSharedFlow.emit(result)
                    }
                    Results.Status.LOADING -> {
                        mutableSharedFlow.emit(Results(Results.Status.LOADING, null, null))
                    }
                    Results.Status.ERROR -> {
                        mutableSharedFlow.emit(Results(Results.Status.ERROR, null, result.error))
                    }
                }
            } catch (e: Throwable) {
                mutableSharedFlow.emit(Results(Results.Status.ERROR, null, e))
            }
        }
    }

    fun searchPokemon(searchName: String) = localDataSource.searchPokemon(searchName)

    suspend fun getAllFavoritePokemon() =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            localDataSource.getAllFavoritePokemon()
        }

    fun getPokemonById(id: Int) = localDataSource.getPokemonById(id)

    suspend fun getPokemonStates(pokemonId: Int): List<PokemonStats> {
        val stats = CoroutineScope(Dispatchers.IO).async {
            localDataSource.getPokemonStates(pokemonId)
        }
        return stats.await()
    }

    suspend fun getPokemonMoves(pokemonId: Int): List<PokemonMove> {
        val stats = CoroutineScope(Dispatchers.IO).async {
            localDataSource.getPokemonMoves(pokemonId)
        }
        return stats.await()
    }

    fun setMoveDescription(pokemonMoveEntity: PokemonMove) {
        localDataSource.setMoveDescription(pokemonMoveEntity)
    }

    fun updatePokemon(pokemonEntity: Pokemon) {
        localDataSource.updatePokemon(pokemonEntity)
    }
}