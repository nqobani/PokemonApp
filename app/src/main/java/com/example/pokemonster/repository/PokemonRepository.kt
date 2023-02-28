package com.example.pokemonster.repository

import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface PokemonRepository {
    fun getAllPokemon(mutableSharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>)
    fun getPokemonById(id: Int, mutableSharedFlow: MutableSharedFlow<Results<PokemonEntity>>)
    fun searchPokemon(
        searchName: String,
        mutableSharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>
    )
    fun getMoveDetails(id: Int, mutableSharedFlow: MutableSharedFlow<Results<MoveResponse>>)
    suspend fun getPokemonStates(pokemonId: Int): Flow<List<PokemonStatEntity>>
    suspend fun getPokemonMoves(pokemonId: Int): Flow<List<PokemonMoveEntity>>
    suspend fun getAllFavoritePokemon(
        mutableSharedFlow: MutableSharedFlow<Results<List<PokemonEntity>>>
    )
    fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity)
    fun updatePokemon(pokemonEntity: PokemonEntity)
}