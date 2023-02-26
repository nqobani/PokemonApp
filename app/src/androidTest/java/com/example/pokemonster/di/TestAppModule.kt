package com.example.pokemonster.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonster.io.local.PokemonDatabase
import com.example.pokemonster.io.remote.PokemonAPI
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Singleton
    @Provides
    fun providePokemonDatabase(@ApplicationContext applicationContext: Context) = Room.inMemoryDatabaseBuilder(
            applicationContext,
            PokemonDatabase::class.java
        ).build()

    @Provides
    fun pokemonApiRetrofit(): PokemonAPI{
        val BASE_URL = "https://pokeapi.co/api/v2/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PokemonAPI::class.java)
    }

    @Provides
    fun pokemonRepository(pokemonAPI: PokemonAPI,
                          pokemonDatabase: PokemonDatabase): PokemonRepository {
        return PokemonRepositoryImpl(pokemonAPI, pokemonDatabase)
    }
}