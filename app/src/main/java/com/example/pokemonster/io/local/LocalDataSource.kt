package com.example.pokemonster.io.local

import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllPokemon(): Flow<List<PokemonEntity>>
    suspend fun getMoveDetails(id: Int): PokemonMoveEntity
    fun searchPokemon(searchName: String): Flow<Results<List<PokemonEntity>>>
    fun getPokemonById(id: Int): Flow<PokemonEntity>
    suspend fun getPokemonStates(pokemonId: Int): List<PokemonStatEntity>
    suspend fun getPokemonMoves(pokemonId: Int): List<PokemonMoveEntity>
    suspend fun getAllFavoritePokemon(): Flow<Results<List<PokemonEntity>>>
    fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity)
    fun updatePokemon(pokemonEntity: PokemonEntity)

    suspend fun addPokemon(pokemon: PokemonEntity)
    suspend fun addMove(move: PokemonMoveEntity)
    suspend fun addStat(stat: PokemonStatEntity)
}