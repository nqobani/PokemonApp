package com.example.pokemonster.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokemonster.R
import com.example.pokemonster.navigation.PokemonScreens
import com.example.pokemonster.viewmodel.PokemonViewModel

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 4.dp) {
            Text(text = stringResource(R.string.home_title))
        }
    }) { contentPadding ->
        HomeContent(navController)
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    pokemonViewModel: PokemonViewModel = hiltViewModel()
) {
    var searchText by remember {
        mutableStateOf(TextFieldValue(""))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 16.dp),
            placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
            value = searchText, onValueChange = { value ->
                searchText = value
                pokemonViewModel.searchPokemon(value.text)
            }, maxLines = 1
        )
        Spacer(modifier = Modifier.padding(8.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth(), content = {
            items(pokemonViewModel.pokemons.value) {
                PokemonCard(it) { id, name, imageUrl ->
                    navController.navigate(route = PokemonScreens.DetailsScreen.name + "/$id/$name?imageUrl=$imageUrl")
                }
            }
        })
    }
}
