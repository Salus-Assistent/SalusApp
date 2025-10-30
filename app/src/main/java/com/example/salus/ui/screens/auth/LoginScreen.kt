package com.example.salus.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email // Ícone para email
import androidx.compose.material.icons.filled.Lock // Ícone para senha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salus.R // Import R
import com.example.salus.ui.components.SalusButton
import com.example.salus.ui.components.SalusTextField
import com.example.salus.ui.theme.Branco // Importe a cor Branca
import com.example.salus.ui.theme.SalusTheme

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit, // Função para ser chamada ao clicar em LOGIN
    onSignUpClick: () -> Unit, // Função para navegar para SignUp
    onForgotPasswordClick: () -> Unit // Função para navegar para Esqueceu Senha
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de Fundo
        Image(
            painter = painterResource(id = R.drawable.background_auth), // Nome da sua imagem de fundo
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Overlay escuro (ajuste a cor e alpha conforme o design)
        Box(
            Modifier
                .fillMaxSize()
                .background(color = Color(0x80156EE4))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_branca), // Use uma versão branca da logo
                contentDescription = "Logo Salus",
                modifier = Modifier
                    .width(278.dp)
                    .height(278.dp)
            )

            // Abas de Seleção
            Row(
                horizontalArrangement = Arrangement.spacedBy(75.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .width(200.dp)
                    .height(25.dp)
            ) {
                Text(
                    text = "LOG IN",
                    color = Branco, // Cor Branca 100%
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                    // Adicionar sublinhado ou outro indicador se houver no design
                )
                Text(
                    text = "SIGN UP",
                    color = Branco.copy(alpha = 0.75f), // Cor Branca 75%
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.clickable { onSignUpClick() } // Navega para SignUp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Email
            SalusTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "E-mail",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.padding(top = 15.dp))

            // Campo Senha
            SalusTextField(
                value = senha,
                onValueChange = { senha = it },
                labelText = "Senha",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation() // Esconde a senha
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Login
            SalusButton(
                onClick = { onLoginClick(email, senha) }, // Chama a função de login
                text = "LOG IN",
                color = MaterialTheme.colorScheme.primary
            )

            // Botão Esqueceu a Senha
            SalusButton(
                onClick = onForgotPasswordClick,
                text = "Esqueceu a Senha?",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

// Preview
@Preview(name = "Light Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreview() {
//    SalusTheme {
//        var text by remember { mutableStateOf("") }
        LoginScreen(onLoginClick = { _, _ -> }, onSignUpClick = {}, onForgotPasswordClick = {})
//    }
}