package com.example.pokemonster.io.remote.models.pokemon

data class Ability(
    val ability: com.example.pokemonster.io.remote.models.pokemon.AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)