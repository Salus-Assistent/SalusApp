package com.example.salus.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

// Precisamos desta anotação para o ExposedDropdownMenuBox
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpClick: (/* TODO: Passar dados do formulário */) -> Unit,
    onLoginClick: () -> Unit // Função para navegar de volta para Login
) {
    // Estados para todos os campos do formulário
    var nomeCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    // Estados para o campo de seleção (Dropdown)
    var isExpanded by remember { mutableStateOf(false) }
    var tipoConta by remember { mutableStateOf("Selecionar") } // Texto inicial
    val tiposDeConta = listOf("Paciente", "Cuidador")

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de Fundo (mesma da LoginScreen)

        Image(
            painter = painterResource(id = R.drawable.background_auth), // Nome da sua imagem de fundo
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(color = Color(0x80156EE4))
        )
        // Overlay escuro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .fillMaxHeight(), // Garante que a coluna tente preencher a altura
            horizontalAlignment = Alignment.CenterHorizontally,
            // Usamos SpaceAround para distribuir melhor os elementos na vertical
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_branca), // Use uma versão branca da logo
                contentDescription = "Logo Salus",
                modifier = Modifier
                    .width(278.dp)
                    .height(278.dp)
                    // Um pouco menor para caber tudo
            )

            // Abas de Seleção (Invertidas)
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(61.dp, Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "LOG IN",
                    color = Branco.copy(alpha = 0.75f), // 75%
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                                .clickable { onLoginClick() } // Navega para Login
                )

                Text(
                    text = "SIGN UP",
                    color = Branco, // 100%
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // --- Campos de Texto ---
            SalusTextField(
                value = nomeCompleto,
                onValueChange = { nomeCompleto = it },
                labelText = "Nome Completo",
                leadingIcon = Icons.Default.Person
            )

            SalusTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "E-mail",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            SalusTextField(
                value = telefone,
                onValueChange = { telefone = it },
                labelText = "Telefone",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            SalusTextField(
                value = senha,
                onValueChange = { senha = it },
                labelText = "Senha",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )

            SalusTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                labelText = "Confirmar Senha",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )

            // --- Campo de Seleção (Dropdown) ---
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // O TextField que aparece como campo de seleção
                OutlinedTextField(
                    value = tipoConta,
                    onValueChange = {},
                    readOnly = true, // Impede digitação
                    label = { Text("Tipo de Conta") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        // Reutilizando as cores do SalusTextField
                        unfocusedContainerColor = Color(0xFFD9D9D9),
                        focusedContainerColor = Color(0xFFD9D9D9),
                        unfocusedBorderColor = Color(0x26000000),
                        focusedBorderColor = com.example.salus.ui.theme.AuthIconeCampo,
                        focusedLabelColor = com.example.salus.ui.theme.AuthIconeCampo,
                        unfocusedLabelColor = com.example.salus.ui.theme.AuthIconeCampo.copy(alpha = 0.7f),
                        focusedTextColor = Color.Black, // Ajuste se necessário
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth().menuAnchor() // Necessário para o dropdown
                )

                // O menu que aparece quando clicado
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    tiposDeConta.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                tipoConta = item
                                isExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Sign Up
            SalusButton(
                onClick = { /* TODO: Chamar onSignUpClick com os dados */ },
                text = "SIGN UP",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Preview
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SignUpScreenPreview() {
//    SalusTheme {
        SignUpScreen(onSignUpClick = {}, onLoginClick = {})
//    }
}