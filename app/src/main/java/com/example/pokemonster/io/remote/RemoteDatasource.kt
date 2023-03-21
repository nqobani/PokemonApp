package com.example.pokemonster.io.remote

import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonListResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonResponse
import com.example.pokemonster.repository.states.Results

interface RemoteDatasource {
    suspend fun getPaginatedPokemonList(offset: Int, limit: Int): Results<PokemonListResponse>
    suspend fun getPokemon(id: Int): Results<PokemonResponse>
    suspend fun getMoveDetails(id: Int): Results<MoveResponse>
}