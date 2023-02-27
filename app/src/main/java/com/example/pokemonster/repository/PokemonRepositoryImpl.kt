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
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException

private const val SOMETHING_WENT_WRONG = "Something went wrong"

private const val UNABLE_TO_GET_MOVE_DETAILS = "Unable to get move details"

private const val MOVE_NOT_FOUND = "Move not found"

private const val POKEMON_NOT_FOUND = "Pokemon not found"

private const val UNABLE_TO_GET_POKEMON = "Unable to get pokemon"

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonAPI: PokemonAPI,
    private val pokemonDatabase: PokemonDatabase
) : PokemonRepository {
    override fun getAllPokemon(): SharedFlow<Results<List<PokemonEntity>>> {
        val sharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
        CoroutineScope(Dispatchers.Default).launch {
            pokemonDatabase.pokemonDao().getAllPokemons().collect { pokemons ->
                if (pokemons.isEmpty()) {
                    getRemotePokemonAndCache(sharedFlow)
                } else {
                    sharedFlow.emit(Results.Success(pokemons))
                }
            }
        }
        return sharedFlow
    }

    override fun getPokemonById(id: Int): SharedFlow<Results<PokemonEntity>> {
        val sharedFlow = MutableSharedFlow<Results<PokemonEntity>>()
        CoroutineScope(Dispatchers.Default).launch {
            pokemonDatabase.pokemonDao().getPokemonById(id).collect { pokemon ->
                sharedFlow.emit(Results.Success(pokemon))
            }
        }
        return sharedFlow
    }

    override fun searchPokemon(searchName: String): SharedFlow<Results<List<PokemonEntity>>> {
        val sharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
        CoroutineScope(Dispatchers.Default).launch {
            pokemonDatabase.pokemonDao().searchPokemons("$searchName%").collect { pokemons ->
                sharedFlow.emit(Results.Success(pokemons))
            }
        }
        return sharedFlow
    }

    private fun getRemotePokemonAndCache(
        sharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            sharedFlow.emit(Results.Loading)
            for (i in 1..100) {
                val call = async(Dispatchers.IO) {
                    pokemonAPI.getPokemon(i)
                }
                try {
                    val response = call.await()
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.IO) {
                            pokemonDatabase.pokemonDao().insertPokemon(response.body()!!.toPokemonEntity())
                            response.body()!!.stats.forEach { stat ->
                                pokemonDatabase.pokemonDao().insertPokemonState(stat.toStateEntity(response.body()!!.id))
                            }
                            response.body()!!.moves.forEach { move ->
                                pokemonDatabase.pokemonDao().insertPokemonMove(move.toMoveEntity(response.body()!!.id))
                            }
                        }
                    } else {
                        if (response.code() == 404) {
                            sharedFlow.emit(Results.OnError(Exception(POKEMON_NOT_FOUND)))
                        } else {
                            sharedFlow.emit(Results.OnError(Exception(UNABLE_TO_GET_POKEMON)))
                        }
                    }
                } catch (e: HttpException) {
                    sharedFlow.emit(Results.OnError(Exception("unable to load Pokemons")))
                } catch (e: IOException) {
                    sharedFlow.emit(Results.OnError(Exception("unable to load Pokemons")))
                } catch (e: Exception) {
                    sharedFlow.emit(Results.OnError(Exception("unable to load Pokemons")))
                }
            }
            pokemonDatabase.pokemonDao().getAllPokemons().collect { pokemons ->
                sharedFlow.emit(Results.Success(pokemons))
            }
        }
    }

    override fun getMoveDetails(id: Int): SharedFlow<Results<MoveResponse>> {
        val sharedFlow = MutableSharedFlow<Results<MoveResponse>>()
        CoroutineScope(Dispatchers.IO).launch {
            sharedFlow.emit(Results.Loading)
            withContext(Dispatchers.Main) {
                try {
                    val response = pokemonAPI.getMoveDetails(id)
                    if (response.isSuccessful && response.body() != null) {
                        sharedFlow.emit(Results.Success(response.body()!!))
                    } else {
                        if (response.code() == 404) {
                            sharedFlow.emit(Results.OnError(Exception(MOVE_NOT_FOUND)))
                        } else {
                            sharedFlow.emit(Results.OnError(Exception(UNABLE_TO_GET_MOVE_DETAILS)))
                        }
                    }
                } catch (e: java.lang.Exception) {
                    sharedFlow.emit(Results.OnError(Exception(SOMETHING_WENT_WRONG)))
                }
            }
        }
        return sharedFlow
    }

    override suspend fun getPokemonStates(pokemonId: Int): Flow<List<PokemonStatEntity>> {
        val call = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonStates(pokemonId)
        }
        return call.await()
    }

    override suspend fun getPokemonMoves(pokemonId: Int): Flow<List<PokemonMoveEntity>> {
        val call = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonMoves(pokemonId)
        }
        return call.await()
    }

    override suspend fun getAllFavoritePokemon(): SharedFlow<Results<List<PokemonEntity>>> {
        val sharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
        CoroutineScope(Dispatchers.Default).launch {
            pokemonDatabase.pokemonDao().getFavoritePokemon().collect { pokemons ->
                sharedFlow.emit(Results.Success(pokemons))
            }
        }
        return sharedFlow
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