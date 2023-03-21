package com.example.pokemonster.io.local

import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocalDataSourceImpl(val pokemonDatabase: PokemonDatabase) : LocalDataSource {
    override fun getAllPokemon() = pokemonDatabase.pokemonDao()
        .getAllPokemons()
        .flowOn(Dispatchers.IO)

    override suspend fun getMoveDetails(id: Int): PokemonMoveEntity {
        val data = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonMove(id)
        }
        return data.await()
    }

    override fun searchPokemon(
        searchName: String
    ) = flow {
        val pokemons = pokemonDatabase.pokemonDao().searchPokemons("$searchName%")
        emit(Results(Results.Status.SUCCESS, pokemons, null))
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(Results(Results.Status.SUCCESS, null, e))
    }

    override suspend fun getPokemonStates(
        pokemonId: Int
    ): List<PokemonStatEntity> {
        val data = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonStates(pokemonId)
        }
        return data.await()
    }

    override suspend fun getPokemonMoves(
        pokemonId: Int
    ): List<PokemonMoveEntity> {
        val call = CoroutineScope(Dispatchers.IO).async {
            pokemonDatabase.pokemonDao().getPokemonMoves(pokemonId)
        }
        return call.await()
    }

    override suspend fun getAllFavoritePokemon() = flow {
        val pokemons = pokemonDatabase.pokemonDao().getFavoritePokemon()
        emit(Results(Results.Status.SUCCESS, pokemons, null))
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(Results(Results.Status.ERROR, null, e))
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

    override suspend fun addPokemon(pokemon: PokemonEntity) {
        pokemonDatabase.pokemonDao().insertPokemon(pokemon)
    }

    override suspend fun addMove(move: PokemonMoveEntity) {
        pokemonDatabase.pokemonDao().insertPokemonMove(move)
    }

    override suspend fun addStat(stat: PokemonStatEntity) {
        pokemonDatabase.pokemonDao().insertPokemonStat(stat)
    }

    override fun getPokemonById(id: Int) = pokemonDatabase.pokemonDao().getPokemonById(id)
}