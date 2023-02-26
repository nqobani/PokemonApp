package com.example.pokemonster.navigation

enum class PokemonScreens {
    HomeScreen,
    DetailsScreen;
    companion object{
        fun fromRoute(route: String?): PokemonScreens = when(route?.substringBefore(delimiter = "/")){
            HomeScreen.name -> HomeScreen
            DetailsScreen.name -> DetailsScreen
            null -> HomeScreen
            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }
    }

}