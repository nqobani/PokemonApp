package com.example.pokemonster.io.local

import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.io.remote.models.pokemon.Move
import com.example.pokemonster.io.remote.models.pokemon.PokemonResponse
import com.example.pokemonster.io.remote.models.pokemon.Stat

fun PokemonResponse.toPokemonEntity(): PokemonEntity {
    return PokemonEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.sprites.other.officialArtwork.front_default,
        isFavorite = false
    )
}

fun Stat.toStateEntity(pokemonId: Int): PokemonStatEntity {
    return PokemonStatEntity(
        id = "${this.stat.name}-$pokemonId",
        pokemonId = pokemonId,
        name = this.stat.name,
        base_stat = this.base_stat
    )
}

fun Move.toMoveEntity(pokemonId: Int): PokemonMoveEntity {
    val slidedUrl = this.move.url.split('/')
    val moveId = slidedUrl[slidedUrl.size - 2]
    return PokemonMoveEntity(
        id = "$moveId-$pokemonId",
        pokemonId = pokemonId,
        moveRemoteId = moveId.toInt(),
        name = this.move.name,
        effect = null
    )
}