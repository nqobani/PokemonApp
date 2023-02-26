package com.example.pokemonster.io.remote.models.moves

data class Meta(
    val ailment: com.example.pokemonster.io.remote.models.moves.Ailment,
    val ailment_chance: Int,
    val category: com.example.pokemonster.io.remote.models.moves.Category,
    val crit_rate: Int,
    val drain: Int,
    val flinch_chance: Int,
    val healing: Int,
    val max_hits: Any,
    val max_turns: Any,
    val min_hits: Any,
    val min_turns: Any,
    val stat_chance: Int
)