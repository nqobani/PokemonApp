package com.example.pokemonster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.repository.PokemonRepository
import com.example.pokemonster.repository.states.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
   private val pokemonRepository: PokemonRepository
): ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
             getAllPokemon().collect{ result ->
                 updatePokemonList(result)
             }
        }
    }

    var pokemons = mutableStateOf<List<PokemonEntity>>(listOf())

    var pokemonStats = mutableStateOf<List<PokemonStatEntity>>(listOf())

    var pokemonMoves = mutableStateOf<List<PokemonMoveEntity>>(listOf())

    var selectedMoveName = mutableStateOf("")

    var selectedMoveEffect = mutableStateOf("")

    private fun updatePokemonList(result: Results<List<PokemonEntity>>) {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                when (result) {
                    is Results.Loading -> {

                    }
                    is Results.Success -> {
                        pokemons.value = result.data
                    }
                    is Results.OnError -> {

                    }
                }
            }
        }
    }

    fun getMoveDetails(move: PokemonMoveEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepository.getMoveDetails(move.moveRemoteId).collect{ moveState ->
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

    private fun getAllPokemon() = pokemonRepository.getAllPokemon()

    fun searchPokemon(searchName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!searchName.isBlank()){
                pokemonRepository.searchPokemon(searchName).collect { results ->
                    updatePokemonList(results)
                }
            } else {
                getAllPokemon().collect { result ->
                    updatePokemonList(result)
                }
            }
        }
    }

    suspend fun getPokemonStates(pokemonId: Int) {
        pokemonRepository.getPokemonStates(pokemonId).collect{ stats ->
            pokemonStats.value = stats
        }
    }

    suspend fun getPokemonMoves(pokemonId: Int){
        pokemonRepository.getPokemonMoves(pokemonId).collect { moves ->
            pokemonMoves.value = moves
        }
    }

    private fun setMoveDescription(pokemonMoveEntity: PokemonMoveEntity) = pokemonRepository.setMoveDescription(pokemonMoveEntity)
}