package ru.lazyhat.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonToken(
    val id: String,
    val lessonId: UInt,
    val expires: LocalDateTime
)

@Serializable
data class UserToken(
    val username: String,
    val access: Access,
    val expiresAt: LocalDateTime
)

enum class RegisterStatus(val description: String?) {
    Idle(null),
    Loading(null),
    Failed("invalid token")
}