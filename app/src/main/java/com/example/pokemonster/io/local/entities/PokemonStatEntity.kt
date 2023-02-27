package com.example.pokemonster.io.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tblStates",
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pokemonId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonStatEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String, // name-pokemonId
    val pokemonId: Int,
    val name: String,
    val base_stat: Int
)