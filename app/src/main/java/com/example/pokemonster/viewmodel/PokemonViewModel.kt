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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    init {
        showAllPokemon()
    }

    var pokemons = mutableStateOf<List<PokemonEntity>>(listOf())

    var pokemonInDetailsView = mutableStateOf<PokemonEntity?>(null)

    var pokemonStats = mutableStateOf<List<PokemonStatEntity>>(listOf())

    var pokemonMoves = mutableStateOf<List<PokemonMoveEntity>>(listOf())

    var selectedMoveName = mutableStateOf("")

    var selectedMoveEffect = mutableStateOf("")

    var isLoading = mutableStateOf(false)

    var showingFavorite = mutableStateOf(false)

    private fun updatePokemonList(result: Results<List<PokemonEntity>>) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                when (result) {
                    is Results.Loading -> {
                        isLoading.value = true
                    }
                    is Results.Success -> {
                        pokemons.value = result.data
                        isLoading.value = false
                    }
                    is Results.OnError -> {
                        isLoading.value = false
                    }
                }
            }
        }
    }

    fun getMoveDetails(move: PokemonMoveEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<MoveResponse>>()
            pokemonRepository.getMoveDetails(move.moveRemoteId, mutableSharedFlow)
            mutableSharedFlow.collect { moveState ->
                when (moveState) {
                    is Results.Loading -> {
                        selectedMoveEffect.value = "Loading..."
                    }
                    is Results.Success -> {
                        val effect = moveState.data.effect_entries[0].effect
                        selectedMoveEffect.value = effect
                        setMoveDescription(move.copy(effect = effect))
                    }
                    is Results.OnError -> {
                        selectedMoveEffect.value = ";-("
                    }
                }
            }
        }
    }

    fun showAllPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            pokemonRepository.getAllPokemon(mutableSharedFlow)
            mutableSharedFlow.collect { result ->
                updatePokemonList(result)
            }
        }
    }

    fun updatePokemon(pokemonEntity: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepository.updatePokemon(pokemonEntity)
        }
    }

    fun searchPokemon(searchName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            if (!searchName.isBlank()) {
                pokemonRepository.searchPokemon(searchName, mutableSharedFlow)
            } else {
                pokemonRepository.getAllPokemon(mutableSharedFlow)
            }
            mutableSharedFlow.collect { result ->
                updatePokemonList(result)
            }
        }
    }

    fun showFavoritePokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            pokemonRepository.getAllFavoritePokemon(mutableSharedFlow)
            mutableSharedFlow.collect { result ->
                updatePokemonList(result)
            }
        }
    }

    fun getPokemonById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<PokemonEntity>>()
            pokemonRepository.getPokemonById(id, mutableSharedFlow)
            mutableSharedFlow.collect { result ->
                when (result) {
                    is Results.Loading -> {
                        isLoading.value = true
                    }
                    is Results.Success -> {
                        pokemonInDetailsView.value = result.data
                        isLoading.value = false
                    }
                    is Results.OnError -> {
                        isLoading.value = false
                    }
                }
            }
        }
    }

    suspend fun getPokemonStates(pokemonId: Int) {
        pokemonRepository.getPokemonStates(pokemonId).collect { stats ->
            pokemonStats.value = stats
        }
    }

    suspend fun getPokemonMoves(pokemonId: Int) {
        pokemonRepository.getPokemonMoves(pokemonId).collect { moves ->
            pokemonMoves.value = moves
        }
    }

    private fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity) = pokemonRepository.setMoveDescription(pokemonMoveEntity)
}