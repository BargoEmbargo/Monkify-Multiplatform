package cz.uhk.monkify.navigation.bottombar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.uhk.monkify.extension.applyBottomBarVerticalPadding
import cz.uhk.monkify.extension.applyHorizontalScreenPaddingAndTabletLimit
import cz.uhk.monkify.navigation.bottombar.model.BottomBarItems
import cz.uhk.monkify.theme.MonkifyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonkifyBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier.fillMaxWidth().applyBottomBarVerticalPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .applyHorizontalScreenPaddingAndTabletLimit()
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primary),
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                BottomBarItems.items.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                }
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.route) },
                        label = null,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.onPrimary,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MonkifyBottomBarPreview() {
    MonkifyTheme {
        MonkifyBottomBar(navController = rememberNavController())
    }
}
