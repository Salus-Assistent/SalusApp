package com.example.salus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.salus.ui.theme.SalusTheme

@Composable
fun SalusButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(50.dp),
        enabled = enabled
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun SalusButtonPreview() {
    SalusTheme {
        SalusButton(
            onClick = {},
            text = "ENTRAR",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun SalusButtonDisabledPreview() {
    SalusTheme {
        SalusButton(
            onClick = {},
            text = "CARREGANDO...",
            enabled = false,
            color = MaterialTheme.colorScheme.primary
        )
    }
}