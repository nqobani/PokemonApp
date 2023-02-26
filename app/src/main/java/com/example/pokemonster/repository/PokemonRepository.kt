package com.example.pokemonster.repository

import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PokemonRepository {
    fun getAllPokemon(): SharedFlow<Results<List<PokemonEntity>>>
    fun searchPokemon(searchName: String): SharedFlow<Results<List<PokemonEntity>>>
    fun getMoveDetails(id: Int): SharedFlow<Results<MoveResponse>>
    suspend fun getPokemonStates(pokemonId: Int): Flow<List<PokemonStatEntity>>
    suspend fun getPokemonMoves(pokemonId: Int): Flow<List<PokemonMoveEntity>>
    fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity)
}