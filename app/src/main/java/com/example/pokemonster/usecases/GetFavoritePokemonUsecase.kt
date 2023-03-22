package com.example.pokemonster.usecases

import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow

class GetFavoritePokemonUsecase(private val pokemonRepository: PokemonRepository) {
    suspend operator fun invoke(): Flow<Results<List<Pokemon>>> {
        return pokemonRepository.getAllFavoritePokemon()
    }
}