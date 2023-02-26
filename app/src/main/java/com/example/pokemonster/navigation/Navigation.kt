package com.example.pokemonster.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.view.components.DetailsScreen
import com.example.pokemonster.view.components.HomeScreen

@Composable
fun PokemonNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PokemonScreens.HomeScreen.name){
        composable(route = PokemonScreens.HomeScreen.name){
            HomeScreen(navController = navController)
        }
        composable(route = PokemonScreens.DetailsScreen.name+"/{pokemonId}/{name}?imageUrl={imageUrl}", arguments = listOf(
            navArgument(name = "pokemonId"){
                type = NavType.IntType
            }, navArgument(name = "imageUrl"){
                type = NavType.StringType
            },navArgument(name = "name"){
                type = NavType.StringType
            }
        )){ backStackEntry ->
            DetailsScreen(
                navController = navController,
                backStackEntry.arguments?.getInt("pokemonId"),
                backStackEntry.arguments?.getString("name"),
                backStackEntry.arguments?.getString("imageUrl")
            )
        }
    }
}