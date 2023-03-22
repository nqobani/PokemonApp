package com.example.pokemonster.usecases

import com.example.pokemonster.model.PokemonStats
import com.example.pokemonster.repository.PokemonRepository

class GetPokemonStatsUsecase(private val pokemonRepository: PokemonRepository) {
    suspend operator fun invoke(pokemonId: Int): List<PokemonStats> {
        return pokemonRepository.getPokemonStates(pokemonId)
    }
}