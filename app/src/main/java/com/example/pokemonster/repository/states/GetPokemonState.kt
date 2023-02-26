package com.example.pokemonster.repository.states

sealed class Results<out T> {
    object Loading: Results<Nothing>()
    data class Success<out R>(val data: R): Results<R>()
    data class OnError(val throwable: Throwable): Results<Nothing>()
}
