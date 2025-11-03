package com.example.salus.ui.screens.auth // Ajuste o pacote se necessário

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.ui.components.SalusButton
import com.example.salus.ui.components.SalusTextField
import com.example.salus.ui.theme.Branco
import com.example.salus.ui.theme.SalusTheme

@Composable
fun ForgotPasswordScreen2(
    onConfirmClick: () -> Unit, // Ação após o pop-up de sucesso
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var novaSenha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    // Estado para controlar o pop-up de sucesso
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Botão "Confirmar" só ativa se as senhas não estiverem vazias E forem iguais
    val isConfirmEnabled = novaSenha.isNotBlank() && novaSenha == confirmarSenha

    // O erro só aparece se o campo "Confirmar Senha" tiver algo
    // E as duas senhas forem diferentes.
    val isSenhaError = confirmarSenha.isNotEmpty() && novaSenha != confirmarSenha

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_auth),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_branca),
                contentDescription = "Logo Salus",
                modifier = Modifier
                    .width(278.dp)
                    .height(278.dp)
            )

            // Abas de Seleção
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(75.dp, Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Entrar",
                    color = Branco.copy(alpha = 0.75f), // 75%
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .clickable { onLoginClick() } // Navega para Login
                )

                Text(
                    text = "Cadastrar",
                    color = Branco.copy(alpha = 0.75f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .clickable {onSignUpClick() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Nova Senha
            SalusTextField(
                value = novaSenha,
                onValueChange = { novaSenha = it },
                labelText = "Nova senha",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                isError = isSenhaError
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Confirmar Senha
            SalusTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                labelText = "Confirmar Senha",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                isError = isSenhaError,
                supportingText = if (isSenhaError) "As senhas não coincidem." else null
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Confirmar (com lógica de ativação)
            SalusButton(
                onClick = {
                    // Mostra o pop-up de sucesso
                    showSuccessDialog = true
                },
                text = "Confirmar",
                color = MaterialTheme.colorScheme.primary,
                enabled = isConfirmEnabled // Ativado apenas se as senhas baterem
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    // Pop-up de Sucesso
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                // Não permite fechar clicando fora
            },
            title = { Text("Sucesso!") },
            text = { Text("Sua senha foi redefinida com sucesso.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onConfirmClick() // Dispara a navegação de volta para o Login
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ForgotPasswordScreen2Preview() {
    SalusTheme {
        ForgotPasswordScreen2(
            onConfirmClick = {},
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}