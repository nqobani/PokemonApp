package com.example.pokemonster.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokemonster.R
import com.example.pokemonster.viewmodel.DetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    navController: NavController,
    pokemonId: Int?,
    name: String?,
    imageUrl: String?,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    LaunchedEffect("DetailsScreen$pokemonId") {
        pokemonId?.let {
            CoroutineScope(Dispatchers.Default).launch {
                detailsViewModel.getPokemonMoves(it)
            }
            CoroutineScope(Dispatchers.Default).launch {
                detailsViewModel.getPokemonStats(it)
            }
            CoroutineScope(Dispatchers.Default).launch {
                detailsViewModel.getPokemonById(it)
            }
        }
    }
    PokemonDetailsContent(
        navController,
        name,
        imageUrl
    )
}

@Composable
private fun PokemonDetailsContent(
    navController: NavController,
    name: String?,
    imageUrl: String?,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 4.dp) {
            Row(horizontalArrangement = Arrangement.Start) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_discription),
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = name ?: stringResource(R.string.txt_details))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.back_discription),
                    tint = if (detailsViewModel.pokemonInDetailsView.value?.isFavorite == true) {
                        Color(0xFF3B8132)
                    } else {
                        Color(0xFFCCE7C9)
                    },
                    modifier = Modifier.clickable {
                        detailsViewModel.pokemonInDetailsView.value?.let { pokemon ->
                            detailsViewModel.updatePokemon(
                                pokemon.copy(isFavorite = !pokemon.isFavorite)
                            )
                        }
                    }
                )
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState(), true, reverseScrolling = true),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                model = imageUrl,
                contentDescription = null
            )
            Text(
                text = name ?: stringResource(R.string.name_missing),
                style = MaterialTheme.typography.h4
            )

            LazyRow(content = {
                items(items = detailsViewModel.pokemonStats.value) {
                    Card(
                        elevation = 1.dp,
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.overline,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                            )
                            Text(
                                text = it.base_stat.toString(),
                                style = MaterialTheme.typography.h5,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                            )
                        }
                    }
                }
            })

            Text(
                text = stringResource(R.string.txt_moves_title),
                style = MaterialTheme.typography.subtitle1
            )
            LazyRow(content = {
                items(items = detailsViewModel.pokemonMoves.value) { move ->
                    Card(
                        elevation = 1.dp,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                detailsViewModel.selectedMoveName.value = move.name
                                detailsViewModel.selectedMoveEffect.value = move.effect ?: ""
                                if (detailsViewModel.selectedMoveEffect.value.isBlank()) {
                                    detailsViewModel.getMoveDetails(move)
                                }
                            }
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = move.name,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                )
                            )
                        }
                    }
                }
            })

            MoveDescriptionView(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                detailsViewModel.selectedMoveName.value,
                detailsViewModel.selectedMoveEffect.value
            )
        }
    }
}

@Composable
private fun MoveDescriptionView(modifier: Modifier, moveName: String, moveEffect: String) {
    Box(
        modifier = modifier
    ) {
        Column {
            Text(text = moveName, style = MaterialTheme.typography.h6)
            Text(text = moveEffect, style = MaterialTheme.typography.caption)
        }
    }
}