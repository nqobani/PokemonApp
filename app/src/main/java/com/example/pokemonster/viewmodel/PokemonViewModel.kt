package com.example.pokemonster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonster.model.Pokemon
import com.example.pokemonster.repository.states.Results
import com.example.pokemonster.usecases.GetFavoritePokemonUsecase
import com.example.pokemonster.usecases.GetPokemonUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonUsecase: GetPokemonUsecase,
    private val getFavoritePokemonUsecase: GetFavoritePokemonUsecase
) : ViewModel() {

    init {
        getAllPokemon()
    }
    var pokemons = mutableStateOf<List<Pokemon>>(listOf())
        get() {
            return when (uiState.value) {
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

    private var pokemonsFavorite = mutableStateOf<List<Pokemon>>(listOf())
    private var pokemonsSearched = mutableStateOf<List<Pokemon>>(listOf())
    private var pokemonsAll = mutableStateOf<List<Pokemon>>(listOf())

    var isLoading = mutableStateOf(false)

    var showingFavorite = mutableStateOf(false)

    var uiState = mutableStateOf(0)
    // TODO: Create an enum for this and give it meaningful names,
    // 0 all, 1 search, 3 favorite

    private fun getAllPokemon() {
        if (uiState != null) {
            uiState.value = 0
        }
        viewModelScope.launch {
            getPokemonUsecase().collect { result ->
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

    fun searchPokemon(searchName: String) {
        viewModelScope.launch {
            if (!searchName.isBlank()) {
                uiState.value = 1
            } else {
                uiState.value = 0
            }
            getPokemonUsecase(searchName).collect { result ->
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

    fun showFavoritePokemon() {
        uiState.value = 2
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritePokemonUsecase().collect { result ->
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