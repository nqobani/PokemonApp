package com.example.pokemonster.io.remote

import com.example.pokemonster.io.remote.models.moves.MoveRemoteResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonListRemoteResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonRemoteResponse
import com.example.pokemonster.repository.states.Results

interface RemoteDatasource {
    suspend fun getPaginatedPokemonList(offset: Int, limit: Int): Results<PokemonListRemoteResponse>
    suspend fun getPokemon(id: Int): Results<PokemonRemoteResponse>
    suspend fun getMoveDetails(id: Int): Results<MoveRemoteResponse>
}