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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.ui.component.ListDialog

@SuppressLint("RememberReturnType")
@Composable
fun RecordScreen(
    recordViewModel: RecordViewModel = hiltViewModel()
) {
    remember {
        recordViewModel.getAllItems()
    }

    val allItems = recordViewModel.allItems
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            itemsIndexed(allItems.value) { index, item ->
                ItemListScreen(item, lastItem = index == allItems.value.lastIndex) {
                    recordViewModel.deleteItem(item)
                    scope.launch {
                        snackbarHostState.showSnackbar("삭제되었습니다")
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
    }
}

@Composable
fun ItemListScreen(item: ItemEntity, lastItem: Boolean, onButtonClicked: () -> Unit) {
    var showListDialog by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
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
                onButtonClicked()
            }
        )
    }
}