package kr.jm.feature_record

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val recordRoute = "record"

fun NavGraphBuilder.recordScreen() {
    composable(route = recordRoute) {
        RecordScreen()
    }
}