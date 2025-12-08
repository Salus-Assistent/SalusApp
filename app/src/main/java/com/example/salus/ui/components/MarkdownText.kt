package com.example.salus.ui.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current
) {
    val annotatedString = parseMarkdown(text, MaterialTheme.typography)
    Text(
        text = annotatedString,
        modifier = modifier,
        style = style,
        color = color
    )
}

private fun parseMarkdown(text: String, typography: Typography): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.split('\n')

        val inlineRegex = Regex("(\\*\\*(.*?)\\*\\*)|(\\*(.*?)\\*)")

        lines.forEachIndexed { index, line ->

            when {
                line.startsWith("### ") -> {
                    withStyle(style = typography.titleMedium.toSpanStyle().copy(fontWeight = FontWeight.Bold)) {
                        append(line.removePrefix("### ").trim())
                    }
                }

                else -> {
                    var currentIndex = 0
                    val matches = inlineRegex.findAll(line)

                    matches.forEach { matchResult ->
                        if (matchResult.range.first > currentIndex) {
                            append(line.substring(currentIndex, matchResult.range.first))
                        }

                        val boldContent = matchResult.groupValues[2].ifEmpty { null }
                        val italicContent = matchResult.groupValues[4].ifEmpty { null }

                        when {
                            boldContent != null -> {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(boldContent)
                                }
                            }
                            italicContent != null -> {
                                withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                    append(italicContent)
                                }
                            }
                        }
                        currentIndex = matchResult.range.last + 1
                    }

                    if (currentIndex < line.length) {
                        append(line.substring(currentIndex, line.length))
                    }
                }
            }

            if (index < lines.size - 1) {
                append('\n')
            }
        }
    }
}