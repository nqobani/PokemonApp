package com.example.pokemonster.io.remote

import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonListResponse
import com.example.pokemonster.io.remote.models.pokemon.PokemonResponse
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class RemoteDatasourceImpl(private val pokemonAPI: PokemonAPI) : RemoteDatasource {
    override suspend fun getPaginatedPokemonList(offset: Int, limit: Int): Results<PokemonListResponse> {
        return try {
            val call = CoroutineScope(Dispatchers.IO).async {
                pokemonAPI.getPaginatedPokemonList(offset, limit)
            }
            val response = call.await()
            if (response.isSuccessful && response.body() != null) {
                Results(Results.Status.SUCCESS, response.body(), null)
            } else {
                Results(
                    Results.Status.ERROR,
                    response.body(),
                    Exception(response.message() ?: "Something went wrong")
                )
            }
        } catch (e: Throwable) {
            Results(Results.Status.ERROR, null, e)
        }
    }

    override suspend fun getPokemon(id: Int): Results<PokemonResponse> {
        return try {
            val call = CoroutineScope(Dispatchers.IO).async {
                pokemonAPI.getPokemon(id)
            }
            val response = call.await()
            if (response.isSuccessful && response.body() != null) {
                Results(Results.Status.SUCCESS, response.body(), null)
            } else {
                Results(
                    Results.Status.ERROR,
                    response.body(),
                    Exception(response.message() ?: "Something went wrong")
                )
            }
        } catch (e: Throwable) {
            Results(Results.Status.ERROR, null, e)
        }
    }

    override suspend fun getMoveDetails(id: Int): Results<MoveResponse> {
        return try {
            val call = CoroutineScope(Dispatchers.IO).async {
                pokemonAPI.getMoveDetails(id)
            }
            val response = call.await()
            if (response.isSuccessful && response.body() != null) {
                Results(Results.Status.SUCCESS, response.body(), null)
            } else {
                Results(
                    Results.Status.ERROR,
                    response.body(),
                    Exception(response.message() ?: "Something went wrong")
                )
            }
        } catch (e: Throwable) {
            Results(Results.Status.ERROR, null, e)
        }
    }
}