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
    // TODO: Adicionar parâmetro para cor/gradiente se necessário
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(50.dp),
        enabled = enabled
        // Se quiser usar gradiente no botão:
        // colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        // contentPadding = PaddingValues() // Remover padding interno se usar background
    ) {
        // Se usar gradiente, envolva o Text com um Box e aplique o background nele:
        // Box(modifier = Modifier.background(Brush.verticalGradient(colors= listOf(AzulGradienteInicio, AzulGradienteFim))).fillMaxSize(), contentAlignment = Alignment.Center) { ... }
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, widthDp = 300) // Definimos uma largura para o preview
@Composable
fun SalusButtonPreview() {
    SalusTheme {
        SalusButton(
            onClick = {}, // Ação vazia no preview
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
            // Exemplo de botão desabilitado
        )
    }
}