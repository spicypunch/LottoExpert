package kr.jm.lottoexpert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.jm.lottoexpert.ui.theme.bgColor
import kr.jm.lottoexpert.ui.component.MyBottomNavigation
import kr.jm.lottoexpert.ui.screen.record.RecordScreen
import kr.jm.lottoexpert.ui.screen.search.SearchScreen

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
    val navController: NavHostController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        modifier = Modifier.background(bgColor),
        bottomBar = {
            if (currentRoute != null && showBottomBar(currentRoute)) {
                MyBottomNavigation(navController)
            }
        }
    ) {
        Box(modifier = Modifier.padding(it).background(bgColor).fillMaxSize()) {
            NavHost(navController = navController, startDestination = "search") {
                composable(route = "search") {
                    SearchScreen()
                }
                composable(route = "record") {
                    RecordScreen()
                }
            }
        }
    }
}



fun showBottomBar(currentRoute: String?): Boolean {
    return currentRoute in listOf("search", "record")
}