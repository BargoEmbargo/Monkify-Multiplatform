package cz.uhk.monkify.screens.plan

data class AchievementProgress(
    val completed: Int,
    val total: Int,
    val uncompleted: Int,
    val percent: Int,
    val pieData: List<Int>,
)
