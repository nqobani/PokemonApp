package com.example.pokemonster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.model.PokemonMove
import com.example.pokemonster.model.PokemonStats
import com.example.pokemonster.repository.states.Results
import com.example.pokemonster.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPokemonMoveUsecase: GetPokemonMoveUsecase,
    private val getPokemonUsecase: GetPokemonUsecase,
    private val getPokemonMovesUsecase: GetPokemonMovesUsecase,
    private val getPokemonStatsUsecase: GetPokemonStatsUsecase,
    private val setPokemonMoveDescriptionUsecase: SetPokemonMoveDescriptionUsecase,
    private val updatePokemonUsecase: UpdatePokemonUsecase
) : ViewModel() {
    var selectedMoveName = mutableStateOf("")
    var pokemonInDetailsView = mutableStateOf<Pokemon?>(null)
    var pokemonMoves = mutableStateOf<List<PokemonMove>>(listOf())
    var selectedMoveEffect = mutableStateOf("")
    var pokemonStats = mutableStateOf<List<PokemonStats>>(listOf())

    fun getMoveDetails(move: PokemonMove) {
        viewModelScope.launch(Dispatchers.IO) {
            getPokemonMoveUsecase(move.moveRemoteId).collect { moveState ->
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
            getPokemonUsecase(id).collect { result ->
                pokemonInDetailsView.value = result
            }
        }
    }

    fun getPokemonStats(pokemonId: Int) {
        viewModelScope.launch {
            pokemonStats.value = getPokemonStatsUsecase(pokemonId)
        }
    }

    suspend fun getPokemonMoves(pokemonId: Int) {
        pokemonMoves.value = getPokemonMovesUsecase(pokemonId)
    }

    private fun setMoveDescription(
        pokemonMove: PokemonMove
    ) = setPokemonMoveDescriptionUsecase(pokemonMove)

    fun updatePokemon(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePokemonUsecase(pokemon)
        }
    }
}