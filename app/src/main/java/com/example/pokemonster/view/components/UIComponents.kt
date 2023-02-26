package com.example.pokemonster.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemonster.R
import com.example.pokemonster.io.local.entities.PokemonEntity



@Composable
fun PokemonCard(pokemonEntity: PokemonEntity,
                onItemClick:(Int, String, String)->Unit) {
    Card(elevation = 4.dp,modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        .clickable {
            onItemClick(
                pokemonEntity.id,
                pokemonEntity.name,
                pokemonEntity.imageUrl
            )
        }) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            AsyncImage(
                modifier = Modifier.width(64.dp),
                model = pokemonEntity.imageUrl,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = pokemonEntity.name,
                style = MaterialTheme.typography.h5)
        }
    }
}
