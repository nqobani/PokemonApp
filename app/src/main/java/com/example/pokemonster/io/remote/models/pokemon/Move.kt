package com.example.pokemonster.io.remote.models.pokemon

data class Move(
    val move: com.example.pokemonster.io.remote.models.pokemon.MoveX,
    val version_group_details: List<com.example.pokemonster.io.remote.models.pokemon.VersionGroupDetail>
)