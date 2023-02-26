package com.example.pokemonster.io.remote.models.pokemon

import com.google.gson.annotations.SerializedName

data class Other(
    val dream_world: com.example.pokemonster.io.remote.models.pokemon.DreamWorld,
    val home: com.example.pokemonster.io.remote.models.pokemon.Home,
    @SerializedName("official-artwork")
    val officialArtwork: com.example.pokemonster.io.remote.models.pokemon.OfficialArtwork
)