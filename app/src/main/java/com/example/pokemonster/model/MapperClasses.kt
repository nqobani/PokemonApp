package com.example.pokemonster.model

import com.example.pokemonster.io.remote.models.pokemon.Move
import com.example.pokemonster.io.remote.models.pokemon.PokemonRemoteResponse
import com.example.pokemonster.io.remote.models.pokemon.Stat

fun PokemonRemoteResponse.toProjectPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        imageUrl = this.sprites.other.officialArtwork.front_default,
        isFavorite = false
    )
}

fun Stat.toProjectPokemonStat(pokemonId: Int): PokemonStats {
    return PokemonStats(
        id = "${this.stat.name}-$pokemonId",
        pokemonId = pokemonId,
        name = this.stat.name,
        base_stat = this.base_stat
    )
}

fun Move.toProjectPokemonMove(pokemonId: Int): PokemonMove {
    val slidedUrl = this.move.url.split('/')
    val moveId = slidedUrl[slidedUrl.size - 2]
    return PokemonMove(
        id = "$moveId-$pokemonId",
        pokemonId = pokemonId,
        moveRemoteId = moveId.toInt(),
        name = this.move.name,
        effect = null
    )
}
