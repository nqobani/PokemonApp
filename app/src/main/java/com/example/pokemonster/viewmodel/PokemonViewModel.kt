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
                        // TODO trigger onError UI
                    }
                }
            }
        }
    }

    fun getAllPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            val mutableSharedFlow = MutableSharedFlow<Results<List<PokemonEntity>>>()
            pokemonRepository.getAllPokemon(mutableSharedFlow)
            mutableSharedFlow.collect { result ->
                updatePokemonList(result)
            }
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
}