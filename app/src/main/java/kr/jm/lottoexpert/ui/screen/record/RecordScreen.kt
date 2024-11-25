package kr.jm.lottoexpert.ui.screen.record

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.ui.component.ListDialog

@SuppressLint("RememberReturnType")
@Composable
fun RecordScreen(
    recordViewModel: RecordViewModel = hiltViewModel()
) {
    val state by recordViewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    RecordScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onEvent = recordViewModel::onEvent
    )
}

@Composable
fun RecordScreenContent(
    state: RecordScreenState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onEvent: (RecordScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = modifier.padding(16.dp)) {
            itemsIndexed(state.items) { index, item ->
                ItemList(item = item, lastItem = index == state.items.lastIndex, onDelete = {
                    onEvent(RecordScreenEvent.DeleteItem(item))
                    scope.launch { snackbarHostState.showSnackbar("삭제되었습니다") }
                })
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun ItemList(
    item: ItemEntity,
    lastItem: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showListDialog by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
            .padding(16.dp)
            .clickable { showListDialog = true }
            .fillMaxWidth()
    ) {
        Text(text = item.name, fontSize = 22.sp)
    }
    if (!lastItem) {
        HorizontalDivider()
    }

    if (showListDialog) {
        val title = "${item.startNum}회차 ~ ${item.endNum}회차"
        ListDialog(
            item.lottoNumbers,
            title = title,
            buttonTitle = "삭제하기",
            buttonEnabled = true,
            onClosed = {
                showListDialog = false
            },
            onButtonClicked = {
                onDelete()
                showListDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecordScreenPreview() {
    val previewState = RecordScreenState(
        items = listOf(
            ItemEntity(
                id = 1,
                name = "1회차 ~ 5회차",
                startNum = "1",
                endNum = "5",
                lottoNumbers = listOf(Pair(1, 3), Pair(2, 4), Pair(3, 2))
            ),
            ItemEntity(
                id = 2,
                name = "6회차 ~ 10회차",
                startNum = "6",
                endNum = "10",
                lottoNumbers = listOf(Pair(4, 2), Pair(5, 3), Pair(6, 1))
            )
        )
    )

    RecordScreenContent(
        state = previewState,
        snackbarHostState = remember { SnackbarHostState() },
        scope = rememberCoroutineScope(),
        onEvent = {}
    )


}

@Preview
@Composable
private fun ItemListPreview() {
    val previewState =
        ItemEntity(
            id = 1,
            name = "1회차 ~ 5회차",
            startNum = "1",
            endNum = "5",
            lottoNumbers = listOf(Pair(1, 3), Pair(2, 4), Pair(3, 2))
        )

    ItemList(item = previewState, lastItem = false, onDelete = {

    })
}