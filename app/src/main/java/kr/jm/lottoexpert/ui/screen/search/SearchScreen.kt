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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.ui.component.CircularProgressBar
import kr.jm.lottoexpert.ui.component.DefaultButton
import kr.jm.lottoexpert.ui.component.DefaultTextField
import kr.jm.lottoexpert.ui.component.ListDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {

    val progressBar by rememberSaveable {
        searchViewModel.progressBarStatus
    }
    val (startNum, setStartNum) = rememberSaveable {
        mutableStateOf("")
    }
    val (endNum, setEndNum) = rememberSaveable {
        mutableStateOf("")
    }
    var showListDialog by remember {
        mutableStateOf(false)
    }
    val lottoNumbers = searchViewModel.lottoNumbers

    val snackbarHostState = remember { SnackbarHostState() }
//    LaunchedEffect(Unit) {
//        val message = searchViewModel.message.value
//        if (message.isNotBlank()) {
//
//            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
//        }
//    }

    val scope = rememberCoroutineScope()
    if (progressBar) {
        CircularProgressBar()
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DefaultTextField(
                            value = startNum,
                            onValueChange = setStartNum,
                            placeholderText = "회차 시작",
                            modifier = Modifier.weight(1f)
                        )
                        DefaultTextField(
                            value = endNum,
                            onValueChange = setEndNum,
                            placeholderText = "회차 끝",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    DefaultButton("조회하기", modifier = Modifier.height(56.dp)) {
                        if (startNum.isBlank() || endNum.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("빈칸을 채워주세요")
                            }
                        } else {
                            val startNumber = startNum.toIntOrNull()
                            val endNumber = endNum.toIntOrNull()

                            if (startNumber == null || endNumber == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("유효한 숫자를 입력해주세요")
                                }
                            } else if (startNumber > endNumber) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("시작 회차가 끝 회차보다 큽니다")
                                }
                            } else {
                                searchViewModel.getLottoNum(startNumber, endNumber)
                                showListDialog = true
                            }
                        }
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
            if (showListDialog) {
                val title = "${startNum}회차 ~ ${endNum}회차"
                ListDialog(
                    lottoNumbers.value,
                    buttonEnabled = true,
                    title = title,
                    buttonTitle = "저장하기",
                    onClosed = {
                        showListDialog = false
                    },
                    onButtonClicked = {
                        searchViewModel.insertItem(
                            name = title,
                            startNum = startNum,
                            endNum = endNum,
                            lottoNumbers = lottoNumbers.value
                        )
                        showListDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("저장되었습니다")
                        }
                    }
                )
            }
        }
    }
}