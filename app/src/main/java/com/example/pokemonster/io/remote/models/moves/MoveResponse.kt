package com.example.pokemonster.io.remote.models.moves

data class MoveResponse(
    val accuracy: Int,
    val contest_combos: Any,
    val contest_effect: com.example.pokemonster.io.remote.models.moves.ContestEffect,
    val contest_type: com.example.pokemonster.io.remote.models.moves.ContestType,
    val damage_class: com.example.pokemonster.io.remote.models.moves.DamageClass,
    val effect_chance: Any,
    val effect_changes: List<Any>,
    val effect_entries: List<com.example.pokemonster.io.remote.models.moves.EffectEntry>,
    val flavor_text_entries: List<com.example.pokemonster.io.remote.models.moves.FlavorTextEntry>,
    val generation: com.example.pokemonster.io.remote.models.moves.Generation,
    val id: Int,
    val learned_by_pokemon: List<com.example.pokemonster.io.remote.models.moves.LearnedByPokemon>,
    val meta: com.example.pokemonster.io.remote.models.moves.Meta,
    val name: String,
    val names: List<com.example.pokemonster.io.remote.models.moves.Name>,
    val power: Int,
    val pp: Int,
    val priority: Int,
    val stat_changes: List<Any>,
    val super_contest_effect: com.example.pokemonster.io.remote.models.moves.SuperContestEffect,
    val target: com.example.pokemonster.io.remote.models.moves.Target,
    val type: com.example.pokemonster.io.remote.models.moves.Type
)