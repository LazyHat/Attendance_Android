package models

import kotlinx.datetime.LocalTime
import ru.lazyhat.models.LessonCreate
import ru.lazyhat.models.LessonUpdate
import kotlin.time.Duration

operator fun LocalTime.plus(duration: Duration): LocalTime = (this.toMillisecondOfDay() + duration.inWholeMilliseconds).let {
    LocalTime.fromMillisecondOfDay(it.toInt())
}

fun LessonCreate.toLessonUpdate(username: String) = LessonUpdate(
    username, title, dayOfWeek, start, duration, groupsList
)