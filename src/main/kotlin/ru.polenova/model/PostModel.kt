package ru.polenova.model

import java.time.ZonedDateTime
import kotlin.Long as Long

data class PostModel (
    val idUser: Long,
    val userName: String? = null,
    val dateOfCreate: ZonedDateTime? = null,
    var statusUser: StatusUser = StatusUser.NONE,
    val linkForPost: String? = null,
    val postName: String? = null,
    val postText: String? = null,
    val idPost: Long,
    var postUpCount: Int,
    var postDownCount: Int,
    var pressedPostUp: Boolean,
    var pressedPostDown: Boolean,
    val user: AuthUserModel? = null,
    val attachment: MediaModel? = null,
    var upUserIdList: MutableList<Long> = mutableListOf(),
    var downUserIdList: MutableList<Long> = mutableListOf()
)

enum class StatusUser {
    PROMOTER,
    HATER,
    NONE
}