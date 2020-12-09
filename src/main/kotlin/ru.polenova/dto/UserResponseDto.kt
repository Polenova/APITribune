package ru.polenova.dto

import ru.polenova.model.AuthUserModel

data class UserResponseDto(val userId: Long, val username: String) {
    companion object {
        fun fromModel(model: AuthUserModel) = UserResponseDto(
            userId = model.idUser,
            username = model.username
        )
    }
}
