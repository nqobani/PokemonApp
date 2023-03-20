package com.example.pokemonster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    init {
        getAllPokemon()
    }
    var pokemons = mutableStateOf<List<PokemonEntity>>(listOf())
        get() {
            return when (uiState.value) {
                0 -> {
                    pokemonsAll
                }
                1 -> {
                    pokemonsSearched
                }
                2 -> {
                    pokemonsFavorite
                }
                else -> {
                    pokemonsAll
                }
            }
        }
        private set

    var pokemonsFavorite = mutableStateOf<List<PokemonEntity>>(listOf())
    var pokemonsSearched = mutableStateOf<List<PokemonEntity>>(listOf())
    var pokemonsAll = mutableStateOf<List<PokemonEntity>>(listOf())

    var isLoading = mutableStateOf(false)

    var showingFavorite = mutableStateOf(false)

    var uiState = mutableStateOf(0)
    // TODO: Create an enum for this and give it meaningful names,
    // 0 all, 1 search, 3 favorite

    fun getAllPokemon() {
        if (uiState != null) {
            uiState.value = 0
        }
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            pokemonRepository.getAllPokemon(mutableSharedFlow)
            mutableSharedFlow.collect { result ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        when (result) {
                            is Results.Loading -> {
                                isLoading.value = true
                            }
                            is Results.Success -> {
                                pokemonsAll.value = result.data
                                isLoading.value = false
                            }
                            is Results.OnError -> {
                                isLoading.value = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun searchPokemon(searchName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            if (!searchName.isBlank()) {
                uiState.value = 1
                pokemonRepository.searchPokemon(searchName, mutableSharedFlow)
            } else {
                uiState.value = 0
            }
            mutableSharedFlow.collect { result ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        when (result) {
                            is Results.Loading -> {
                                isLoading.value = true
                            }
                            is Results.Success -> {
                                pokemonsSearched.value = result.data
                                isLoading.value = false
                            }
                            is Results.OnError -> {
                                isLoading.value = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun showFavoritePokemon() {
        uiState.value = 2
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            pokemonRepository.getAllFavoritePokemon(mutableSharedFlow)
            mutableSharedFlow.collect { result ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        when (result) {
                            is Results.Loading -> {
                                isLoading.value = true
                            }
                            is Results.Success -> {
                                pokemonsFavorite.value = result.data
                                isLoading.value = false
                            }
                            is Results.OnError -> {
                                isLoading.value = false
                            }
                        }
                    }
                }
            }
        }
    }
}