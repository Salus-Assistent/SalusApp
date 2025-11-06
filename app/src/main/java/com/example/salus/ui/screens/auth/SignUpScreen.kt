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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.PersonSearch
import com.example.salus.ui.components.CorBordaCampo
import com.example.salus.ui.components.CorFundoCampo
import com.example.salus.ui.theme.AuthIconeCampo

// Precisamos desta anotação para o ExposedDropdownMenuBox
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpClick: (userRole: String) -> Unit,
    onLoginClick: () -> Unit // Função para navegar de volta para Login
) {
    //Estados para todos os campos do formulário
    var nomeCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    // Estados para o campo de seleção (Dropdown)
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val tiposDeConta = listOf("Paciente", "Cuidador")
    var tipoContaSelecionado by remember { mutableStateOf("Selecionar") }

    //Lógica de Validação
//    val senhasCoincidem = senha.isNotBlank() && senha == confirmarSenha
//    val isFormValid = nomeCompleto.isNotBlank() &&
//            email.isNotBlank() &&
//            telefone.isNotBlank() &&
//            senhasCoincidem &&
//            tipoContaSelecionado != "Selecionar"
    val isFormValid = tipoContaSelecionado != "Selecionar"

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
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),// Garante que a coluna tente preencher a altura
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
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
            ) {
                OutlinedTextField(
                    value = tipoContaSelecionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Conta") },
                    leadingIcon = { Icon(Icons.Default.PersonSearch, contentDescription = null, tint = AuthIconeCampo) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    shape = RoundedCornerShape(size = 15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = CorFundoCampo,
                        focusedContainerColor = CorFundoCampo,
                        errorContainerColor = CorFundoCampo,
                        unfocusedBorderColor = CorBordaCampo,
                        focusedBorderColor = AuthIconeCampo,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = AuthIconeCampo,
                        focusedLabelColor = AuthIconeCampo,
                        unfocusedLabelColor = AuthIconeCampo.copy(alpha = 0.7f),
                        focusedLeadingIconColor = AuthIconeCampo,
                        unfocusedLeadingIconColor = AuthIconeCampo
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    tiposDeConta.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoContaSelecionado = tipo
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Cadastrar
            SalusButton(
                onClick = { onSignUpClick(tipoContaSelecionado) },
                text = "Cadastrar",
                enabled = isFormValid,
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