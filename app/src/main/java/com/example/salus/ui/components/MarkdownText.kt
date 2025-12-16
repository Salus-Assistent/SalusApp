package com.example.salus.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null
) {
    val styledText = buildAnnotatedString {
        // Divide o texto pelos asteriscos duplos (**)
        val parts = text.split("**")

        parts.forEachIndexed { index, part ->
            // Se o índice for ímpar (1, 3, 5...), é a parte que estava entre asteriscos
            if (index % 2 == 1) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(part)
                }
            } else {
                // Se for par, é texto normal
                append(part)
            }
        }
    }

    Text(
        text = styledText,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = LocalTextStyle.current
    )
}