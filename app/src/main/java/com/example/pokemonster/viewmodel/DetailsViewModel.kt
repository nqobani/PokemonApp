package com.example.pokemonster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.io.remote.models.moves.MoveResponse
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    var selectedMoveName = mutableStateOf("")
    var pokemonInDetailsView = mutableStateOf<PokemonEntity?>(null)
    var pokemonMoves = mutableStateOf<List<PokemonMoveEntity>>(listOf())
    var selectedMoveEffect = mutableStateOf("")
    var pokemonStats = mutableStateOf<List<PokemonStatEntity>>(listOf())

    fun getMoveDetails(move: PokemonMoveEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<MoveResponse>>()
            pokemonRepository.getMoveDetails(move.moveRemoteId, mutableSharedFlow)
            mutableSharedFlow.collect { moveState ->
                when (moveState.status) {
                    Results.Status.SUCCESS -> {
                        moveState.data?.let {
                            val effect = moveState.data.effect_entries[0].effect
                            selectedMoveEffect.value = effect
                            setMoveDescription(move.copy(effect = effect))
                        }
                    }
                    Results.Status.LOADING -> {
                        selectedMoveEffect.value = "Loading..."
                    }
                    Results.Status.ERROR -> {
                        selectedMoveEffect.value = ";-("
                    }
                }
            }
        }
    }

    fun getPokemonById(id: Int) {
        viewModelScope.launch {
            pokemonRepository.getPokemonById(id).collect { result ->
                pokemonInDetailsView.value = result
            }
        }
    }

    fun getPokemonStats(pokemonId: Int) {
        viewModelScope.launch {
            pokemonStats.value = pokemonRepository.getPokemonStates(pokemonId)
        }
    }

    suspend fun getPokemonMoves(pokemonId: Int) {
        pokemonMoves.value = pokemonRepository.getPokemonMoves(pokemonId)
    }

    private fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity) = pokemonRepository.setMoveDescription(
        pokemonMoveEntity
    )

    fun updatePokemon(pokemonEntity: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepository.updatePokemon(pokemonEntity)
        }
    }
}