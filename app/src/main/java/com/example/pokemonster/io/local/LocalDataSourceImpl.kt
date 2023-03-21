package com.example.pokemonster.io.local

import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.model.PokemonMove
import com.example.pokemonster.model.PokemonStats
import com.example.pokemonster.model.toProjectPokemon
import com.example.pokemonster.model.toProjectPokemonMove
import com.example.pokemonster.model.toProjectPokemonStats
import com.example.pokemonster.model.toRoomEntity
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LocalDataSourceImpl(val pokemonDatabase: PokemonDatabase) : LocalDataSource {
    override fun getAllPokemon() = pokemonDatabase.pokemonDao()
        .getAllPokemons()
        .map { list ->
            list.map {
                it.toProjectPokemon()
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun getMoveDetails(id: Int): PokemonMove {
        val data = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonMove(id)
        }
        return data.await().toProjectPokemonMove()
    }

    override fun searchPokemon(
        searchName: String
    ) = flow {
        val pokemons = pokemonDatabase.pokemonDao().searchPokemons("$searchName%").map {
            it.toProjectPokemon()
        }
        emit(Results(Results.Status.SUCCESS, pokemons, null))
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(Results(Results.Status.SUCCESS, null, e))
    }

    override suspend fun getPokemonStates(
        pokemonId: Int
    ): List<PokemonStats> {
        val data = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonStates(pokemonId).map {
                it.toProjectPokemonStats()
            }
        }
        return data.await()
    }

    override suspend fun getPokemonMoves(
        pokemonId: Int
    ): List<PokemonMove> {
        val call = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonMoves(pokemonId).map {
                it.toProjectPokemonMove()
            }
        }
        return call.await()
    }

    override suspend fun getAllFavoritePokemon() = flow {
        val pokemons = pokemonDatabase.pokemonDao().getFavoritePokemon().map {
            it.toProjectPokemon()
        }
        emit(Results(Results.Status.SUCCESS, pokemons, null))
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(Results(Results.Status.ERROR, null, e))
    }

    override fun setMoveDescription(pokemonMove: PokemonMove) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDatabase.pokemonDao().setMoveEffectDescription(pokemonMove.toRoomEntity())
        }
    }

    override fun updatePokemon(pokemon: Pokemon) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDatabase.pokemonDao().updatePokemon(pokemon.toRoomEntity())
        }
    }

    override suspend fun addPokemon(pokemon: Pokemon) {
        pokemonDatabase.pokemonDao().insertPokemon(pokemon.toRoomEntity())
    }

    override suspend fun addMove(move: PokemonMove) {
        pokemonDatabase.pokemonDao().insertPokemonMove(move.toRoomEntity())
    }

    override suspend fun addStat(stat: PokemonStats) {
        pokemonDatabase.pokemonDao().insertPokemonStat(stat.toRoomEntity())
    }

    override fun getPokemonById(id: Int) = pokemonDatabase.pokemonDao().getPokemonById(id).map {
        it.toProjectPokemon()
    }
}