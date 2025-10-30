package com.example.salus.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salus.R // Importe o R do seu projeto para acessar os drawables
import com.example.salus.ui.theme.SalusTheme // Importe o seu tema
import kotlinx.coroutines.delay

// Define um tempo de exibição para a splash screen em milissegundos
private const val SPLASH_TIMEOUT = 2000L // 2 segundos

@Composable
fun SplashScreen(
    // Recebe uma função lambda que será chamada para navegar para a próxima tela
    onTimeout: () -> Unit
) {
    // LaunchedEffect executa a corrotina quando o Composable entra na composição
    // A chave 'true' garante que execute apenas uma vez
    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT) // Aguarda o tempo definido
        onTimeout() // Chama a função de navegação
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Usa a cor de fundo definida no nosso tema claro
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center // Centraliza o conteúdo (a logo)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Certifique-se que sua logo está em res/drawable com este nome
            contentDescription = "Logo Salus",
            modifier = Modifier
                .size(300.dp) // Ajuste o tamanho conforme necessário
        )
    }
}
