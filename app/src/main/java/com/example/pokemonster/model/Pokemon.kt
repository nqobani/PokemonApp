package com.example.pokemonster.model

import com.example.pokemonster.io.local.entities.PokemonEntity

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val isFavorite: Boolean
)

fun Pokemon.toRoomEntity(): PokemonEntity {
    return PokemonEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite
    )
}

fun PokemonEntity.toProjectPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite
    )
}