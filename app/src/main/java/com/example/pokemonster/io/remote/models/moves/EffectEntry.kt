package com.example.pokemonster.io.remote.models.moves

data class EffectEntry(
    val effect: String,
    val language: com.example.pokemonster.io.remote.models.moves.Language,
    val short_effect: String
)