package com.example.pokemonster.io.local

import androidx.room.*
import com.example.pokemonster.io.local.entities.FavoritePokemonsEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    /////////////////////POKEMON////////////////////////
    @Query("SELECT * FROM tblPokemon")
    fun getAllPokemons(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM tblPokemon WHERE name LIKE :query")
    fun searchPokemons(query: String): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPokemons(vararg pokemonEntity: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(pokemonEntity: PokemonEntity)

    @Delete
    fun deletePokemon(pokemonEntity: PokemonEntity)

    @Delete
    fun deleteAllPokemon(vararg pokemonEntity: PokemonEntity)

    ///////////////////////FAVORITE/////////////////////////

    @Query("SELECT tblPokemon.id, tblPokemon.name, tblPokemon.imageUrl FROM tblPokemon LEFT JOIN tblFavorite ON tblPokemon.id = tblFavorite.pokemonId")
    fun getFavoritePokemons(): Flow<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favoritePokemonsEntity: FavoritePokemonsEntity)

    @Delete
    fun removeFromFavorite(favoritePokemonsEntity: FavoritePokemonsEntity)
    ///////////////////////STAT//////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStates(vararg pokemonEntity: PokemonStatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonState(pokemonEntity: PokemonStatEntity)

    @Query("SELECT * FROM tblStates where pokemonId=:pokemonId")
    fun getPokemonStates(pokemonId: Int): Flow<List<PokemonStatEntity>>

    //////////////////////////MOVES////////////////////////////
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAllPokemonMoves(vararg pokemonMoveEntity: PokemonMoveEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPokemonMove(pokemonMoveEntity: PokemonMoveEntity)

    @Update(PokemonMoveEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun setMoveEffectDescription(pokemonMoveEntity: PokemonMoveEntity)

    @Query("SELECT * FROM tblPokemonMove where pokemonId=:pokemonId")
    fun getPokemonMoves(pokemonId: Int): Flow<List<PokemonMoveEntity>>

    @Query("SELECT * FROM tblPokemonMove where id=:id")
    fun getPokemonMove(id: Int): Flow<PokemonMoveEntity>
}