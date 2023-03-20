package com.example.pokemonster.io.local

import androidx.room.*
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    // ///////////////////POKEMON////////////////////////
    @Query("SELECT * FROM tblPokemon")
    fun getAllPokemons(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM tblPokemon WHERE name LIKE :query")
    suspend fun searchPokemons(query: String): List<PokemonEntity>

    @Query("SELECT * FROM tblPokemon WHERE id = :id")
    fun getPokemonById(id: Int): Flow<PokemonEntity>

    @Query("SELECT * FROM tblPokemon WHERE isFavorite = true")
    suspend fun getFavoritePokemon(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemons(vararg pokemonEntity: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemonEntity: PokemonEntity)

    @Update(PokemonEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePokemon(pokemonEntity: PokemonEntity)

    @Delete
    suspend fun deletePokemon(pokemonEntity: PokemonEntity)

    @Query("DELETE FROM tblPokemon")
    suspend fun clearData()
    // /////////////////////STAT//////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStates(vararg pokemonEntity: PokemonStatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonState(pokemonEntity: PokemonStatEntity)

    @Query("SELECT * FROM tblStates where pokemonId=:pokemonId")
    suspend fun getPokemonStates(pokemonId: Int): List<PokemonStatEntity>

    // ////////////////////////MOVES////////////////////////////
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAllPokemonMoves(vararg pokemonMoveEntity: PokemonMoveEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPokemonMove(pokemonMoveEntity: PokemonMoveEntity)

    @Update(PokemonMoveEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun setMoveEffectDescription(pokemonMoveEntity: PokemonMoveEntity)

    @Query("SELECT * FROM tblPokemonMove where pokemonId=:pokemonId")
    suspend fun getPokemonMoves(pokemonId: Int): List<PokemonMoveEntity>

    @Query("SELECT * FROM tblPokemonMove where id=:id")
    suspend fun getPokemonMove(id: Int): PokemonMoveEntity
}