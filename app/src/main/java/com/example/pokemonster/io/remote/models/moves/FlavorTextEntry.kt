package com.example.pokemonster.io.remote.models.moves

data class FlavorTextEntry(
    val flavor_text: String,
    val language: com.example.pokemonster.io.remote.models.moves.Language,
    val version_group: com.example.pokemonster.io.remote.models.moves.VersionGroup
)