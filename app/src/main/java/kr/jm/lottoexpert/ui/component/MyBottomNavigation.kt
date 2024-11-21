package kr.jm.lottoexpert.ui.component

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kr.jm.lottoexpert.data.domain.BottomNavItem
import kr.jm.lottoexpert.ui.theme.bgColor
import kr.jm.lottoexpert.ui.theme.borderLineColor
import kr.jm.lottoexpert.ui.theme.textColor

@Composable
fun MyBottomNavigation(navController: NavController) {
    val navItems = listOf(
        BottomNavItem.Search,
        BottomNavItem.Record
    )
    NavigationBar(
        contentColor = bgColor,
        containerColor = bgColor,
        modifier = Modifier.background(bgColor)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        navItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == screen.route) screen.selectedIcon else screen.basicIcon,
                        tint = if (currentRoute == screen.route) borderLineColor else Color.Gray,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(text = screen.title)

                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = borderLineColor,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = borderLineColor,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = bgColor,
                ),
            )

        }
    }
}