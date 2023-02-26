package com.example.pokemonster.io.remote.models.pokemon

data class PokemonResponse(
    val abilities: List<com.example.pokemonster.io.remote.models.pokemon.Ability>,
    val base_experience: Int,
    val forms: List<com.example.pokemonster.io.remote.models.pokemon.Form>,
    val game_indices: List<com.example.pokemonster.io.remote.models.pokemon.GameIndice>,
    val height: Int,
    val held_items: List<Any>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<com.example.pokemonster.io.remote.models.pokemon.Move>,
    val name: String,
    val order: Int,
    val past_types: List<Any>,
    val species: com.example.pokemonster.io.remote.models.pokemon.Species,
    val sprites: com.example.pokemonster.io.remote.models.pokemon.Sprites,
    val stats: List<com.example.pokemonster.io.remote.models.pokemon.Stat>,
    val types: List<com.example.pokemonster.io.remote.models.pokemon.Type>,
    val weight: Int
)