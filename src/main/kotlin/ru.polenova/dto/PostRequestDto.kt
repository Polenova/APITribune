package ru.polenova.dto

import java.time.ZonedDateTime

class PostRequestDto (
    //val idPost: Long,
    val postName: String? = null,
    val postText: String? = null,
    val linkForPost: String? = null,
    val dateOfCreate: ZonedDateTime? = null,
    val attachmentId: String? = null
)