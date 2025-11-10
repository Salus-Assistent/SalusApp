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

/**
 * Um Composable que renderiza uma string de texto com formatação básica de Markdown.
 *
 * Suporta as seguintes sintaxes:
 * - Títulos: `### Título`
 * - Negrito: `**texto em negrito**`
 * - Itálico: `*texto em itálico*`
 * - Quebras de linha.
 *
 * @param text A string contendo o texto em Markdown a ser renderizado.
 * @param modifier O [Modifier] a ser aplicado ao Composable de texto.
 * @param style O estilo de texto a ser aplicado (padrão: `LocalTextStyle.current`).
 * @param color A cor do texto (padrão: `LocalContentColor.current`).
 */
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

/**
 * Função interna que converte uma string de Markdown em um [AnnotatedString].
 *
 * @param text O texto bruto com a formatação Markdown.
 * @param typography O objeto [Typography] do tema para estilizar os títulos.
 * @return Um [AnnotatedString] com os estilos de negrito, itálico e títulos aplicados.
 */
private fun parseMarkdown(text: String, typography: Typography): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.split('\n')

        // Regex para encontrar `**negrito**` (grupo 2) ou `*itálico*` (grupo 4).
        val inlineRegex = Regex("(\\*\\*(.*?)\\*\\*)|(\\*(.*?)\\*)")

        lines.forEachIndexed { index, line ->

            when {
                // Converte linhas que começam com '### ' para títulos.
                line.startsWith("### ") -> {
                    withStyle(style = typography.titleMedium.toSpanStyle().copy(fontWeight = FontWeight.Bold)) {
                        append(line.removePrefix("### ").trim())
                    }
                }

                // Processa texto normal em busca de formatação inline.
                else -> {
                    var currentIndex = 0
                    val matches = inlineRegex.findAll(line)

                    matches.forEach { matchResult ->
                        // Adiciona o texto que vem ANTES da formatação.
                        if (matchResult.range.first > currentIndex) {
                            append(line.substring(currentIndex, matchResult.range.first))
                        }

                        // Extrai o conteúdo de negrito ou itálico.
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
                        // Atualiza o cursor para depois da formatação encontrada.
                        currentIndex = matchResult.range.last + 1
                    }

                    // Adiciona qualquer texto restante na linha após a última formatação.
                    if (currentIndex < line.length) {
                        append(line.substring(currentIndex, line.length))
                    }
                }
            }

            // Adiciona a quebra de linha de volta, se não for a última linha.
            if (index < lines.size - 1) {
                append('\n')
            }
        }
    }
}