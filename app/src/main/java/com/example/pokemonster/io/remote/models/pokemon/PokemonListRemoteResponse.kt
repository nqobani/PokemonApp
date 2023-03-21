package com.example.pokemonster.io.remote.models.pokemon

data class PokemonListRemoteResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)