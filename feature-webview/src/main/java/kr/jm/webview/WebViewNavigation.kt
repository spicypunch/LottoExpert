package kr.jm.webview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val webViewRoute = "webview"

fun NavGraphBuilder.webViewScreen() {
    composable(route = webViewRoute) {
        WebViewScreen()
    }
}