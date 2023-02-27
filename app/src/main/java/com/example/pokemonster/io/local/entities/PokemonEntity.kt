package com.example.pokemonster.io.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblPokemon")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val imageUrl: String,
    val isFavorite: Boolean
)
