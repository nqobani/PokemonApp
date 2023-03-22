package com.example.pokemonster.usecases

import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GetPokemonUsecase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(): Flow<Results<List<Pokemon>>> {
        val mutableSharedFlow = MutableSharedFlow<Results<List<Pokemon>>>()
        pokemonRepository.getAllPokemon(mutableSharedFlow)
        return mutableSharedFlow.asSharedFlow()
    }

    operator fun invoke(searchString: String): Flow<Results<List<Pokemon>>> {
        return pokemonRepository.searchPokemon(searchString)
    }

    operator fun invoke(id: Int): Flow<Pokemon> {
        return pokemonRepository.getPokemonById(id)
    }
}
