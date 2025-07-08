package kr.jm.feature_search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kt.jm.common_ui.AdMobBanner
import kt.jm.common_ui.CircularProgressBar
import kt.jm.common_ui.DefaultButton
import kt.jm.common_ui.DefaultTextField
import kt.jm.common_ui.ListDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {
    val state by searchViewModel.state
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()) {
            snackbarHostState.showSnackbar(state.message, duration = SnackbarDuration.Short)
            searchViewModel.onEvent(SearchScreenEvent.ClearMessage)
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressBar()
        }
    } else {
        SearchScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onEvent = searchViewModel::onEvent
        )
    }
}

@Composable
fun SearchScreenContent(
    state: SearchScreenState,
    snackbarHostState: SnackbarHostState,
    onEvent: (SearchScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                AdMobBanner(
                    adUnitId = "ca-app-pub-3940256099942544/6300978111" // 테스트 광고 ID
                )
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ) {
                    DefaultTextField(
                        value = state.startNum,
                        onValueChange = { onEvent(SearchScreenEvent.UpdateStartNum(it)) },
                        placeholderText = "회차 시작",
                        modifier = modifier.weight(1f)
                    )
                    DefaultTextField(
                        value = state.endNum,
                        onValueChange = { onEvent(SearchScreenEvent.UpdateEndNum(it)) },
                        placeholderText = "회차 끝",
                        modifier = modifier.weight(1f)
                    )
                }
                DefaultButton("조회하기", modifier = modifier.height(56.dp)) {
                    onEvent(SearchScreenEvent.ValidateInput(state.startNum, state.endNum))
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
            if (state.showDialog) {
                val title = "${state.startNum}회차 ~ ${state.endNum}회차"
                ListDialog(
                    lottoNumbers = state.lottoNumbers,
                    buttonEnabled = true,
                    title = title,
                    buttonTitle = "저장하기",
                    onClosed = {
                        onEvent(SearchScreenEvent.CloseDialog)
                    },
                    onButtonClicked = {
                        onEvent(
                            SearchScreenEvent.InsertItem(
                                name = title,
                                startNum = state.startNum,
                                endNum = state.endNum,
                                lottoNumbers = state.lottoNumbers
                            )
                        )
                        onEvent(SearchScreenEvent.CloseDialog)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val previewState = SearchScreenState(
        startNum = "1",
        endNum = "5",
        lottoNumbers = listOf(Pair(1, 3), Pair(2, 4), Pair(3, 2))
    )

    SearchScreenContent(
        state = previewState,
        snackbarHostState = remember { SnackbarHostState() },
        onEvent = {}
    )
}