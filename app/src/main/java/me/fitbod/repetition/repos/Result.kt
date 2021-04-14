package me.fitbod.repetition.repos

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val cause: Throwable) : Result<T>()
}
