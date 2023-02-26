package com.example.pokemonster.io.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonster.io.local.entities.FavoritePokemonsEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity

@Database(entities = [PokemonEntity::class,
    PokemonMoveEntity::class,
    FavoritePokemonsEntity::class,
    PokemonStatEntity::class], version = 2)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}