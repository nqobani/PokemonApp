package com.example.pokemonster.io.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblFavorite")
data class FavoritePokemonsEntity(
    @PrimaryKey(autoGenerate = false)
    val pokemonId: Int
)