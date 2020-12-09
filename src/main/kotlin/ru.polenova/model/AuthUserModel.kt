package ru.polenova.model

import io.ktor.auth.Principal

data class AuthUserModel (
        val idUser: Long = 0,
        val idPost: Long = 0,
        val username: String,
        val password: String,
        val firebaseToken: String? = null
): Principal