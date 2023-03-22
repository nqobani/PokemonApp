package com.example.pokemonster.usecases

import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.repository.PokemonRepository

class UpdatePokemonUsecase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(pokemon: Pokemon) {
        return pokemonRepository.updatePokemon(pokemon)
    }
}