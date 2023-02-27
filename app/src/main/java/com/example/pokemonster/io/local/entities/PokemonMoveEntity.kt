package com.example.pokemonster.io.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tblPokemonMove",
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pokemonId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonMoveEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String, // moveRemoteId-pokemonId
    val pokemonId: Int,
    val moveRemoteId: Int,
    val name: String,
    val effect: String?
)
