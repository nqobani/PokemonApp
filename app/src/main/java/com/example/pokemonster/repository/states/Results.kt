package com.example.pokemonster.repository.states

data class Results<out T>(val status: Status, val data: T?, val error: Throwable?) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    companion object {
        fun <T> success(data: T): Results<T> {
            return Results(Status.SUCCESS, data, null)
        }

        fun <T> loading(data: T): Results<T> {
            return Results(Status.LOADING, data, null)
        }

        fun <T> error(error: Throwable?, data: T): Results<T> {
            return Results(Status.ERROR, data, error)
        }
    }
}

fun <T> Results<T>.isLoading(): Boolean {
    return this.status == Results.Status.LOADING
}

fun <T> Results<T>.isSuccess(): Boolean {
    return this.status == Results.Status.SUCCESS
}

fun <T> Results<T>.isError(): Boolean {
    return this.status == Results.Status.ERROR
}
