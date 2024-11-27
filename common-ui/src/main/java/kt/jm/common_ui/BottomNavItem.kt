package kt.jm.common_ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val basicIcon: ImageVector,
    val selectedIcon: ImageVector,
) {
    data object Search : BottomNavItem(
        route = "search",
        title = "검색",
        basicIcon = Icons.Filled.Search,
        selectedIcon = Icons.Outlined.Search
    )

    data object Record : BottomNavItem(
        route = "record",
        title = "기록",
        basicIcon = Icons.Filled.Edit,
        selectedIcon = Icons.Outlined.Edit
    )
}