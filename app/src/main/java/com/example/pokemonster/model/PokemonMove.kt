package com.example.pokemonster.model

import com.example.pokemonster.io.local.entities.PokemonMoveEntity

data class PokemonMove(
    val id: String,
    val pokemonId: Int,
    val moveRemoteId: Int,
    val name: String,
    val effect: String?
)

fun PokemonMove.toRoomEntity(): PokemonMoveEntity {
    return PokemonMoveEntity(
        id = this.id,
        pokemonId = this.pokemonId,
        moveRemoteId = this.moveRemoteId,
        name = this.name,
        effect = this.effect
    )
}

fun PokemonMoveEntity.toProjectPokemonMove(): PokemonMove {
    return PokemonMove(
        id = this.id,
        pokemonId = this.pokemonId,
        moveRemoteId = this.moveRemoteId,
        name = this.name,
        effect = this.effect
    )
}
