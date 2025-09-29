package cz.uhk.monkify.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.ReadMore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.PhoneLocked
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.ui.graphics.vector.ImageVector
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.category_exercise
import monkifymultiplatform.composeapp.generated.resources.category_meditating
import monkifymultiplatform.composeapp.generated.resources.category_other
import monkifymultiplatform.composeapp.generated.resources.category_phone_locked
import monkifymultiplatform.composeapp.generated.resources.category_reading
import monkifymultiplatform.composeapp.generated.resources.category_riding_bike
import monkifymultiplatform.composeapp.generated.resources.category_running
import monkifymultiplatform.composeapp.generated.resources.category_skin_care
import monkifymultiplatform.composeapp.generated.resources.category_studying
import org.jetbrains.compose.resources.StringResource

enum class CategoryTask(val titleRes: StringResource, val icon: ImageVector) {
    Exercise(Res.string.category_exercise, Icons.Default.SportsMartialArts),
    Studying(Res.string.category_studying, Icons.AutoMirrored.Filled.LibraryBooks),
    Running(Res.string.category_running, Icons.AutoMirrored.Filled.DirectionsRun),
    Meditating(Res.string.category_meditating, Icons.Default.SelfImprovement),
    SkinCare(Res.string.category_skin_care, Icons.Default.Face),
    PhoneLocked(Res.string.category_phone_locked, Icons.Default.PhoneLocked),
    Reading(Res.string.category_reading, Icons.AutoMirrored.Filled.ReadMore),
    RidingBike(Res.string.category_riding_bike, Icons.Default.PedalBike),
    Other(Res.string.category_other, Icons.Default.MoreHoriz),
}
