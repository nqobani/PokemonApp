package com.example.pokemonster.io.remote.models.pokemon

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)