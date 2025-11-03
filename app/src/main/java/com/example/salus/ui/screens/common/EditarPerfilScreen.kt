package com.example.salus.ui.screens.common // Ajuste o pacote se necessário

import android.widget.Toast // <-- IMPORT para o Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // <-- IMPORT para o Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.ui.components.SalusButton
import com.example.salus.ui.components.SalusTextField
import com.example.salus.ui.theme.SalusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavHostController) {

    // Contexto para mostrar o Toast
    val context = LocalContext.current

    // Estados para os campos (simulados)
    var nome by remember { mutableStateOf("Mateus Kenji") }
    var telefone by remember { mutableStateOf("(11) 98765-4321") }
    var email by remember { mutableStateOf("kaeledit@gmail.com") }
    var senhaAntiga by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }
    var confirmarNovaSenha by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") }, // Título aqui é útil
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Botão Voltar
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Fundo transparente
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // --- Avatar (Clicável para Toast) ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        // Opção A: Mostrar Toast
                        Toast
                            .makeText(context, "Funcionalidade em breve!", Toast.LENGTH_SHORT)
                            .show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // TODO: Adicionar um ícone de "câmera" sobreposto
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Campos de Dados Pessoais ---
            SalusTextField(
                value = nome,
                onValueChange = { nome = it },
                labelText = "Nome Completo",
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(8.dp))
            SalusTextField(
                value = telefone,
                onValueChange = { telefone = it },
                labelText = "Telefone",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )
            Spacer(modifier = Modifier.height(8.dp))
            SalusTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "E-mail",
                leadingIcon = Icons.Default.Email,
            )

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            // --- Campos de Mudar Senha (Opção B) ---
            Text(
                text = "Mudar Senha",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            SalusTextField(
                value = senhaAntiga,
                onValueChange = { senhaAntiga = it },
                labelText = "Senha Antiga",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            SalusTextField(
                value = novaSenha,
                onValueChange = { novaSenha = it },
                labelText = "Nova Senha",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            SalusTextField(
                value = confirmarNovaSenha,
                onValueChange = { confirmarNovaSenha = it },
                labelText = "Confirmar Nova Senha",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Botão Salvar ---
            SalusButton(
                onClick = {
                    // Opção 2: Mostrar Toast e Voltar
                    Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                text = "Salvar Alterações",
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun EditarPerfilScreenPreview() {
    SalusTheme {
        EditarPerfilScreen(navController = rememberNavController())
    }
}