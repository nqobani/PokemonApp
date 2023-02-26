package com.example.pokemonster.io.remote.models.pokemon

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: com.example.pokemonster.io.remote.models.pokemon.StatX
)