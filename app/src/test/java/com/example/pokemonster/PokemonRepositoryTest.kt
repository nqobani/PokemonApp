@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pokemonster

import app.cash.turbine.test
import com.example.pokemonster.io.local.PokemonDatabase
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.remote.PokemonAPI
import com.example.pokemonster.repository.PokemonRepositoryImpl
import com.example.pokemonster.repository.states.Results
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PokemonRepositoryTest {
    val pokemonApi: PokemonAPI = mockk()
    val pokemonDatabase: PokemonDatabase = mockk()

    @Test
    fun `get pokemon successfully`() = runTest {
        val repo = PokemonRepositoryImpl(pokemonApi, pokemonDatabase)

        val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
        repo.getAllPokemon(mutableSharedFlow)
        mutableSharedFlow.test {
            val firstEmit = awaitItem()
            assert(firstEmit is Results.Loading)
            awaitComplete()
        }
    }
}