package kr.jm.lottoexpert.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.jm.lottoexpert.ui.theme.primaryColor
import kr.jm.lottoexpert.ui.theme.textColor

@Composable
fun DefaultButton(
    title: String,
    onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .height(64.dp)
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