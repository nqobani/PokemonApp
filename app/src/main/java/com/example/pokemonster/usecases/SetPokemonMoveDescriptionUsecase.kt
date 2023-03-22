package com.example.pokemonster.usecases

import com.example.pokemonster.model.PokemonMove
import com.example.pokemonster.repository.PokemonRepository

class SetPokemonMoveDescriptionUsecase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(pokemonMove: PokemonMove) {
        return pokemonRepository.setMoveDescription(pokemonMove)
    }
}