package cz.uhk.monkify.database.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    var daysCompleted: Int = 0,
    var lastActivityDate: String = "",
)
