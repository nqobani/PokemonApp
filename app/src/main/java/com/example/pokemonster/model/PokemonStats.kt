package com.example.pokemonster.model

import com.example.pokemonster.io.local.entities.PokemonStatEntity

data class PokemonStats(
    val id: String,
    val pokemonId: Int,
    val name: String,
    val base_stat: Int
)

fun PokemonStats.toRoomEntity(): PokemonStatEntity {
    return PokemonStatEntity(
        id = this.id,
        pokemonId = this.pokemonId,
        name = this.name,
        base_stat = this.base_stat
    )
}

fun PokemonStatEntity.toProjectPokemonStats(): PokemonStats {
    return PokemonStats(
        id = this.id,
        pokemonId = this.pokemonId,
        name = this.name,
        base_stat = this.base_stat
    )
}