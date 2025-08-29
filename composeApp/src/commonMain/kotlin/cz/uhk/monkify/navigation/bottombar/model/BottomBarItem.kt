package cz.uhk.monkify.navigation.bottombar.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.ui.graphics.vector.ImageVector
import cz.uhk.monkify.navigation.NavigationGraph

data class BottomBarItem(val route: String, val icon: ImageVector)

object BottomBarItems {
    val items = listOf(
        BottomBarItem(NavigationGraph.HomeScreen.name, Icons.Default.Home),
        BottomBarItem(NavigationGraph.PlanScreen.name, Icons.Outlined.Checklist),
    )
}
