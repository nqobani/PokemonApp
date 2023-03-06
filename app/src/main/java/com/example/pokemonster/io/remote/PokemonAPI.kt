package com.example.pokemonster.io.remote

import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonListResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPaginatedPokemonList(@Query("offset") offset: Int, @Query("limit") limit: Int): Response<PokemonListResponse>

    @GET("pokemon/{id}/")
    suspend fun getPokemon(@Path("id") id: Int): Response<PokemonResponse>

    @GET("move/{id}/")
    suspend fun getMoveDetails(@Path("id") id: Int): Response<MoveResponse>
}
