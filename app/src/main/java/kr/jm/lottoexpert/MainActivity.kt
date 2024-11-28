package kr.jm.lottoexpert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.jm.feature_record.recordScreen
import kr.jm.feature_search.searchRoute
import kr.jm.feature_search.searchScreen
import kr.jm.webview.webViewScreen
import kt.jm.common_ui.BottomNavItem
import kt.jm.common_ui.MyBottomNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(BottomNavItem.Search.route, BottomNavItem.Record.route)) {
                MyBottomNavigation(
                    items = listOf(
                        BottomNavItem.Search,
                        BottomNavItem.WebView,
                        BottomNavItem.Record
                    ),
                    selectedRoute = currentRoute ?: BottomNavItem.Search.route,
                    onItemSelected = { route -> navController.navigate(route) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = searchRoute
            ) {
                searchScreen()
                webViewScreen()
                recordScreen()
            }
        }
    }
}