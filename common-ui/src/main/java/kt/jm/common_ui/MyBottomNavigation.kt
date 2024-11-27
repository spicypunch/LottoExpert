package kt.jm.common_ui

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kt.jm.common_ui.theme.bgColor
import kt.jm.common_ui.theme.borderLineColor

@Composable
fun MyBottomNavigation(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        contentColor = bgColor,
        containerColor = bgColor,
        modifier = modifier.background(bgColor)
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = selectedRoute == screen.route,
                onClick = { onItemSelected(screen.route) },
                icon = {
                    Icon(
                        imageVector = if (selectedRoute == screen.route) {
                            screen.selectedIcon
                        } else {
                            screen.basicIcon
                        },
                        tint = if (selectedRoute == screen.route) {
                            borderLineColor
                        } else {
                            Color.Gray
                        },
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
                    indicatorColor = bgColor
                )
            )
        }
    }
}