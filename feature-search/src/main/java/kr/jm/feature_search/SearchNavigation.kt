package kr.jm.feature_search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val searchRoute = "search"

fun NavGraphBuilder.searchScreen() {
    composable(route = searchRoute) {
        SearchScreen()
    }
}