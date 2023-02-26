package com.example.pokemonster.io.remote.models.pokemon

data class VersionGroupDetail(
    val level_learned_at: Int,
    val move_learn_method: com.example.pokemonster.io.remote.models.pokemon.MoveLearnMethod,
    val version_group: com.example.pokemonster.io.remote.models.pokemon.VersionGroup
)