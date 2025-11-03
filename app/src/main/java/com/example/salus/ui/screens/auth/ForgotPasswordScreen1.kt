package com.example.salus.ui.screens.auth // Ajuste o pacote se necessário

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salus.R
import com.example.salus.ui.components.SalusButton
import com.example.salus.ui.components.SalusTextField
import com.example.salus.ui.theme.Branco
import com.example.salus.ui.theme.SalusTheme

@Composable
fun ForgotPasswordScreen1(
    onConfirmClick: () -> Unit, // Navega para o próximo ecrã
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }

    // Estado para o botão de Enviar/Reenviar Código
    var textoBotaoCodigo by remember { mutableStateOf("Enviar Código") }

    // O botão "Confirmar" só é ativado se o código for "1234"
    val isConfirmEnabled = codigo == "1234"

    // O erro só aparece se o utilizador escreveu algo E o código não é "1234"
    val isCodigoError = codigo.isNotEmpty() && !isConfirmEnabled

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

            // Campo Email
            SalusTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "E-mail",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Código
            SalusTextField(
                value = codigo,
                onValueChange = { codigo = it },
                labelText = "Código",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Number,
                isError = isCodigoError,
                supportingText = if (isCodigoError) "Código de verificação inválido." else null
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Enviar/Reenviar Código
            SalusButton(
                onClick = {
                    // Apenas muda o texto, como pedido
                    textoBotaoCodigo = "Reenviar Código"
                },
                text = textoBotaoCodigo,
                color = MaterialTheme.colorScheme.primary
            )

            // Botão Confirmar (com lógica de ativação)
            SalusButton(
                onClick = onConfirmClick,
                text = "Confirmar",
                color = MaterialTheme.colorScheme.primary,
                enabled = isConfirmEnabled // Ativado apenas se o código for "1234"
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ForgotPasswordScreen1Preview() {
    SalusTheme {
        ForgotPasswordScreen1(
            onConfirmClick = {},
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}