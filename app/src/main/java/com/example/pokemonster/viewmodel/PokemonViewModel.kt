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
                        when (result.status) {
                            Results.Status.SUCCESS -> {
                                result.data?.let {
                                    pokemonsAll.value = it
                                }
                                isLoading.value = false
                            }
                            Results.Status.LOADING -> {
                                isLoading.value = true
                            }
                            Results.Status.ERROR -> {
                                isLoading.value = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun searchPokemon(searchName: String) {
        viewModelScope.launch {
            if (!searchName.isBlank()) {
                uiState.value = 1
            } else {
                uiState.value = 0
            }
            pokemonRepository.searchPokemon(searchName).collect { result ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        when (result.status) {
                            Results.Status.SUCCESS -> {
                                result.data?.let {
                                    pokemonsSearched.value = it
                                }
                                isLoading.value = false
                            }
                            Results.Status.LOADING -> {
                                isLoading.value = true
                            }
                            Results.Status.ERROR -> {
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
            pokemonRepository.getAllFavoritePokemon().collect { result ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        when (result.status) {
                            Results.Status.SUCCESS -> {
                                result.data?.let {
                                    pokemonsFavorite.value = it
                                }
                                isLoading.value = false
                            }
                            Results.Status.LOADING -> {
                                isLoading.value = true
                            }
                            Results.Status.ERROR -> {
                                isLoading.value = false
                            }
                        }
                    }
                }
            }
        }
    }
}