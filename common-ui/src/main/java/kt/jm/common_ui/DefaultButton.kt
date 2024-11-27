package kt.jm.common_ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kt.jm.common_ui.theme.primaryColor
import kt.jm.common_ui.theme.textColor

@Composable
fun DefaultButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryColor
        ),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onClick()
        }) {
        Text(text = title, color = textColor)
    }
}