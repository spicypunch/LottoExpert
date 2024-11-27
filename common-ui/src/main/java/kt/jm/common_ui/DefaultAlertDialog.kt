package kt.jm.common_ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun RemoveDialog(onClicked: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { onClicked(false) },
        title = { Text(text = "삭제하시겠습니까?") },
        text = { Text(text = "기록하신 번호가 삭제됩니다") },
        confirmButton = {
            TextButton(onClick = { onClicked(true) }) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            TextButton(onClick = { onClicked(false) }) {
                Text(text = "취소")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}