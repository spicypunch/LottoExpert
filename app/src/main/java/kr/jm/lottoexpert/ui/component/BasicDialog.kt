package kr.jm.lottoexpert.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kr.jm.lottoexpert.ui.screen.search.SearchViewModel

@Composable
fun ResultDialog(
    lottoNumbers: MutableState<List<Pair<Int, Int>>>,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onClicked: (Boolean) -> Unit,
    showRecommendDialog: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { onClicked(false) }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.height(650.dp)
            ) {
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
                            ListItem(
                                headlineContent = { Text("번호: ${item.first}") },
                                supportingContent = { Text("당첨 횟수: ${item.second}") },
//                                leadingContent = { Text(text = "추천") },
//                                trailingContent = { Text(text = "cnrk") }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                DefaultButton("기록하기") {

                }
            }
        }
    }
}