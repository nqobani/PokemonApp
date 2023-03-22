package com.example.pokemonster.usecases

import com.example.pokemonster.model.PokemonMove
import com.example.pokemonster.repository.PokemonRepository

class GetPokemonMovesUsecase(private val pokemonRepository: PokemonRepository) {
    suspend operator fun invoke(id: Int): List<PokemonMove> {
        return pokemonRepository.getPokemonMoves(id)
    }
}