package com.example.pokemonster.io.remote

import com.example.pokemonster.io.remote.models.moves.MoveRemoteResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonListRemoteResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonRemoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPaginatedPokemonList(@Query("offset") offset: Int, @Query("limit") limit: Int): Response<PokemonListRemoteResponse>

    @GET("pokemon/{id}/")
    suspend fun getPokemon(@Path("id") id: Int): Response<PokemonRemoteResponse>

    @GET("move/{id}/")
    suspend fun getMoveDetails(@Path("id") id: Int): Response<MoveRemoteResponse>
}
