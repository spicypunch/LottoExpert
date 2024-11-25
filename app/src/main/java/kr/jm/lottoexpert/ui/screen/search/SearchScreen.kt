package kr.jm.lottoexpert.ui.screen.search

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.ui.component.CircularProgressBar
import kr.jm.lottoexpert.ui.component.DefaultButton
import kr.jm.lottoexpert.ui.component.DefaultTextField
import kr.jm.lottoexpert.ui.component.ListDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {
    val state by searchViewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()) {
            snackbarHostState.showSnackbar(state.message, duration = SnackbarDuration.Short)
            searchViewModel.onEvent(SearchScreenEvent.ClearMessage)
        }
    }

    SearchScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onEvent = searchViewModel::onEvent
    )
}

@Composable
fun SearchScreenContent(
    state: SearchScreenState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onEvent: (SearchScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showListDialog by remember {
        mutableStateOf(false)
    }
    if (state.isLoading) {
        CircularProgressBar()
    } else {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Scaffold { innerPadding ->
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
                        when {
                            state.startNum.isBlank() || state.endNum.isBlank() -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("빈칸을 채워주세요")
                                }
                            }

                            state.startNum.toIntOrNull() == null || state.endNum.toIntOrNull() == null -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("유효한 숫자를 입력해주세요")
                                }
                            }

                            state.startNum.toInt() > state.endNum.toInt() -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("시작 회차가 끝 회차보다 큽니다")
                                }
                            }

                            else -> {
                                onEvent(
                                    SearchScreenEvent.GetLottoNum(
                                        state.startNum.toInt(),
                                        state.endNum.toInt()
                                    )
                                )
                                showListDialog = true
                            }
                        }
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )
                if (showListDialog) {
                    val title = "${state.startNum}회차 ~ ${state.endNum}회차"
                    ListDialog(
                        lottoNumbers = state.lottoNumbers,
                        buttonEnabled = true,
                        title = title,
                        buttonTitle = "저장하기",
                        onClosed = {
                            showListDialog = false
                        },
                        onButtonClicked = {
                            onEvent(SearchScreenEvent.InsertItem(
                                name = title,
                                startNum = state.startNum,
                                endNum = state.endNum,
                                lottoNumbers = state.lottoNumbers
                            ))
                            showListDialog = false
                        }
                    )
                }
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
        scope = rememberCoroutineScope(),
        onEvent = {}
    )
}