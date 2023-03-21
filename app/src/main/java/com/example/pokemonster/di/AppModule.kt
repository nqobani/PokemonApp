package com.example.pokemonster.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonster.io.local.LocalDataSource
import com.example.pokemonster.io.local.LocalDataSourceImpl
import com.example.pokemonster.io.local.PokemonDatabase
import com.example.pokemonster.io.remote.PokemonAPI
import com.example.pokemonster.io.remote.RemoteDatasource
import com.example.pokemonster.io.remote.RemoteDatasourceImpl
import com.example.pokemonster.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providePokemonDatabase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        PokemonDatabase::class.java,
        "pokemon_db"
    ).build()

    @Provides
    fun pokemonApiRetrofit(): PokemonAPI {
        val BASE_URL = "https://pokeapi.co/api/v2/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PokemonAPI::class.java)
    }

    @Provides
    fun pokemonRepository(
        remoteDatasource: RemoteDatasource,
        localDataSource: LocalDataSource
    ) = PokemonRepository(remoteDatasource, localDataSource)

    @Provides
    fun provideLocalDataSource(pokemonDatabase: PokemonDatabase):
        LocalDataSource = LocalDataSourceImpl(pokemonDatabase)

    @Provides
    fun provideRemoteDataSource(pokemonAPI: PokemonAPI):
        RemoteDatasource = RemoteDatasourceImpl(pokemonAPI)
}