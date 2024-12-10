package com.pdm.cher.viewmodels

sealed class FirebaseResult<out T> {
    data class Success<out T>(val data: T) : FirebaseResult<T>()
    data class Error(val message: String?) : FirebaseResult<Nothing>()
}