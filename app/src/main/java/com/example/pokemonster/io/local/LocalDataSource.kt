package com.example.pokemonster.io.local

import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.model.PokemonMove
import com.example.pokemonster.model.PokemonStats
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllPokemon(): Flow<List<Pokemon>>
    suspend fun getMoveDetails(id: Int): PokemonMove
    fun searchPokemon(searchName: String): Flow<Results<List<Pokemon>>>
    fun getPokemonById(id: Int): Flow<Pokemon>
    suspend fun getPokemonStates(pokemonId: Int): List<PokemonStats>
    suspend fun getPokemonMoves(pokemonId: Int): List<PokemonMove>
    suspend fun getAllFavoritePokemon(): Flow<Results<List<Pokemon>>>
    fun setMoveDescription(pokemonMove: PokemonMove)
    fun updatePokemon(pokemon: Pokemon)

    suspend fun addPokemon(pokemon: Pokemon)
    suspend fun addMove(move: PokemonMove)
    suspend fun addStat(stat: PokemonStats)
}