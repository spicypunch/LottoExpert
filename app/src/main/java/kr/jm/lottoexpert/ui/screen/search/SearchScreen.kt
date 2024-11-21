package kr.jm.lottoexpert.ui.screen.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.ui.component.CircularProgressBar
import kr.jm.lottoexpert.ui.component.DefaultButton
import kr.jm.lottoexpert.ui.component.ResultDialog
import kr.jm.lottoexpert.ui.theme.primaryColor
import kr.jm.lottoexpert.ui.theme.textColor
import kr.jm.lottoexpert.ui.theme.textFieldBgColor
import kr.jm.lottoexpert.ui.theme.textFieldColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {

    val progressBar by rememberSaveable {
        searchViewModel.progressBarStatus
    }
    val (startDrawNumber, setStartDrawNumber) = rememberSaveable {
        mutableStateOf("")
    }
    val (endDrawerNumber, setEndDrawerNumber) = rememberSaveable {
        mutableStateOf("")
    }
    var showResultDialog by remember {
        mutableStateOf(false)
    }
    var showRecommendDialog by remember {
        mutableStateOf(false)
    }
    val lottoNumbers = searchViewModel.lottoNumbers
    val snackbarHostState = remember { SnackbarHostState() }
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
                        OutlinedTextField(
                            value = startDrawNumber,
                            onValueChange = setStartDrawNumber,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(80.dp)
                                .weight(1f)
                                .background(textFieldBgColor),
                            placeholder = { Text(text = "회차 시작") },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textFieldColor,
                                focusedContainerColor = textFieldBgColor,
                                unfocusedContainerColor = textFieldBgColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = textFieldColor
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        OutlinedTextField(
                            value = endDrawerNumber,
                            onValueChange = setEndDrawerNumber,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(80.dp)
                                .weight(1f)
                                .background(textFieldBgColor),
                            placeholder = { Text(text = "회차 끝") },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textFieldColor,
                                focusedContainerColor = textFieldBgColor,
                                unfocusedContainerColor = textFieldBgColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = textFieldColor,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                    DefaultButton("조회하기") {
                        if (startDrawNumber.isBlank() || endDrawerNumber.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("빈칸을 채워주세요")
                            }
                        } else {
                            val startNum = startDrawNumber.toIntOrNull()
                            val endNum = endDrawerNumber.toIntOrNull()

                            if (startNum == null || endNum == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("유효한 숫자를 입력해주세요")
                                }
                            } else if (startNum > endNum) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("시작 회차가 끝 회차보다 큽니다")
                                }
                            } else {
                                searchViewModel.getLottoNum(startNum, endNum)
                                showResultDialog = true
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

            if (showResultDialog) {
                ResultDialog(
                    lottoNumbers,
                    onClicked = {
                        showResultDialog = it
                    },
                    showRecommendDialog = {
                        showRecommendDialog = it
                    }
                )
            }
        }
    }
}