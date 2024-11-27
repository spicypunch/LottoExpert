package kt.jm.common_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ListDialog(
    lottoNumbers: List<Pair<Int, Int>>,
    title: String,
    buttonTitle: String = "확인",
    buttonEnabled: Boolean = false,
    onClosed: () -> Unit,
    onButtonClicked: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = { onClosed() }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .height(650.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(lottoNumbers) { item ->
                        ListItem(
                            headlineContent = { Text("번호: ${item.first}") },
                            supportingContent = { Text("당첨 횟수: ${item.second}") },
                        )
                    }
                }
                if (buttonEnabled)
                    DefaultButton(buttonTitle) {
                        if (onButtonClicked != null) {
                            onButtonClicked()
                        }
                    }
            }
        }
    }
}