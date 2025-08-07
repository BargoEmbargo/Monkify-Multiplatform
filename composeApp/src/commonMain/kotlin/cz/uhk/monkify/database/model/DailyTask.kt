package cz.uhk.monkify.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.uhk.monkify.util.Constants

@Entity(Constants.DATABASE_TABLE_NAME)
data class DailyTask(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var isChecked: Boolean = false,
    var descriptionText: String,
    var categoryTask: String,
)
