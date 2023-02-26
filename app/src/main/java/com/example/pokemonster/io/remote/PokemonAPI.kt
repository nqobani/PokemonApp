package com.example.pokemonster.io.remote

import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonAPI {
    @GET("pokemon/{id}/")
    suspend fun getPokemon(@Path("id") id: Int): Response<com.example.pokemonster.io.remote.models.pokemon.PokemonResponse>

    @GET("move/{id}/")
    suspend fun getMoveDetails(@Path("id") id: Int): Response<com.example.pokemonster.io.remote.models.moves.MoveResponse>
}

