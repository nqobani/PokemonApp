@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pokemonster

import app.cash.turbine.test
import com.example.pokemonster.io.local.PokemonDatabase
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.remote.PokemonAPI
import com.example.pokemonster.io.remote.models.pokemon.PokemonListRemoteResponse
import com.example.pokemonster.io.remote.models.pokemon.Result
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class PokemonRepositoryTest {
    val pokemonApi: PokemonAPI = mockk()
    val pokemonDatabase: PokemonDatabase = mockk()

    @Test
    fun `get pokemon successfully`() = runTest {
        val response: Response<PokemonListRemoteResponse> = Response.success(
            PokemonListRemoteResponse(
                count = 0,
                next = "",
                previous = 0,
                results = listOf(Result(name = "Pik", "fdf/1/"), Result(name = "Fik", "fdf/2/"))
            )
        )
        val repo = PokemonRepository(pokemonApi, pokemonDatabase)

        val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
        coEvery { pokemonDatabase.pokemonDao() }
        coEvery { pokemonApi.getPaginatedPokemonList(0, 100) } returns response
        coEvery { pokemonApi.getPokemon(any()) } returns mockk()
        repo.getAllPokemon(mutableSharedFlow)
        mutableSharedFlow.test {
            val firstEmit = awaitItem()
            assert(firstEmit is Results.Loading)
            awaitComplete()
        }
    }
}