package kr.jm.lottoexpert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kr.jm.lottoexpert.ui.theme.LottoExpertTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoExpertTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App(
    viewModel: ViewModel = hiltViewModel()
) {
    val progressBar by rememberSaveable {
        viewModel.progressBarStatus
    }
    val (startDrawNumber, setStartDrawNumber) = rememberSaveable {
        mutableStateOf("1")
    }
    val (endDrawerNumber, setEndDrawerNumber) = rememberSaveable {
        mutableStateOf("1100")
    }
    var showResultDialog by remember {
        mutableStateOf(false)
    }
    var showRecommendDialog by remember {
        mutableStateOf(false)
    }
    val lottoNumbers = viewModel.lottoNumbers

    if (progressBar) {
        CircularProgressBar()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = startDrawNumber,
                    onValueChange = setStartDrawNumber,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(80.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(text = "회부터")
                OutlinedTextField(
                    value = endDrawerNumber,
                    onValueChange = setEndDrawerNumber,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(80.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(text = "회까지")
            }
            Button(onClick = {
                viewModel.getLottoNum(
                    startDrawNumber.toInt(),
                    endDrawerNumber.toInt()
                )
                showResultDialog = true
            }) {
                Text(text = "조회하기")
            }
        }
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
        if (showRecommendDialog) {
            RecommendDialog(onClicked = {
                showRecommendDialog = it
            })
        }
    }
}

@Composable
fun ResultDialog(
    lottoNumbers: MutableState<List<Pair<Int, Int>>>,
    viewModel: ViewModel = hiltViewModel(),
    onClicked: (Boolean) -> Unit,
    showRecommendDialog: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { onClicked(false) }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight(0.9f)
                        .fillMaxWidth(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(lottoNumbers.value) { item ->
                            Row {
                                Text(text = "당첨 숫자: ${item.first}")
                                Text(text = "당첨 횟수: ${item.second}")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.recommendLottoNumbers()
                        onClicked(false)
                        showRecommendDialog(true)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "위 번호로 로또 번호 조합 하기")
                }
            }
        }
    }
}

@Composable
fun RecommendDialog(
    viewModel: ViewModel = hiltViewModel(),
    onClicked: (Boolean) -> Unit
) {
    val recommendNumbers = viewModel.recommendNumbers
    Dialog(onDismissRequest = { onClicked(false) }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(1f)
            ) {
                Row {
                    Text(text = "${recommendNumbers.value.get(0)}", modifier = Modifier.padding(8.dp))
                    Text(text = "${recommendNumbers.value.get(1)}", modifier = Modifier.padding(8.dp))
                    Text(text = "${recommendNumbers.value.get(2)}", modifier = Modifier.padding(8.dp))
                    Text(text = "${recommendNumbers.value.get(3)}", modifier = Modifier.padding(8.dp))
                    Text(text = "${recommendNumbers.value.get(4)}", modifier = Modifier.padding(8.dp))
                    Text(text = "${recommendNumbers.value.get(5)}", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun CircularProgressBar() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun PreviewScreen() {
    App()
}