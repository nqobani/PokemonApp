package com.example.pokemonster.usecases

import com.example.pokemonster.io.remote.models.moves.MoveRemoteResponse
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GetPokemonMoveUsecase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(moveRemoteId: Int): Flow<Results<MoveRemoteResponse>> {
        val mutableSharedFlow = MutableSharedFlow<Results<MoveRemoteResponse>>()
        pokemonRepository.getMoveDetails(moveRemoteId, mutableSharedFlow)
        return mutableSharedFlow.asSharedFlow()
    }
}