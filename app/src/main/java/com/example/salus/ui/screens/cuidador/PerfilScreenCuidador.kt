package com.example.salus.ui.screens.cuidador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // <-- IMPORT
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.* // <-- Importa os novos componentes
import com.example.salus.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class) // Necessário para o TopAppBar
@Composable
fun PerfilScreenCuidador(navController: NavHostController) {

    var showLogoutDialog by remember { mutableStateOf(false) }
    var alertasNotif by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Vazio */ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Fundo transparente
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        // REMOVEMOS a bottomBar e o floatingActionButton

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Spacer(modifier = Modifier.height(40.dp))

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mateus Kenji", // Nome do Cuidador (Simulado)
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {

                    ProfileOptionButton(
                        text = "Editar Perfil",
                        icon = Icons.Default.Edit,
                        onClick = { navController.navigate(AppScreens.EditarPerfil.route) }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    // A OPÇÃO ESPECÍFICA DO CUIDADOR
                    ProfileOptionButton(
                        text = "Gerir Pacientes",
                        icon = Icons.Default.Group,
                        onClick = { navController.navigate(AppScreens.RegistrosCuidador.route) }
                    )

                    // Notificações (Integrado)
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = "Notificações",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        NotificationToggle(
                            text = "Alertas de AVC (Pacientes)",
                            checked = alertasNotif,
                            onCheckedChange = { alertasNotif = it }
                        )
                    }

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileOptionButton(
                        text = "Ajuda & Tutorial",
                        icon = Icons.Default.HelpOutline,
                        onClick = { navController.navigate(AppScreens.AjudaTutorial.route) }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileOptionButton(
                        text = "Privacidade & Termos",
                        icon = Icons.Default.Lock,
                        onClick = { navController.navigate(AppScreens.PrivacidadeTermos.route) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Logout
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Logout", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Diálogo de Logout
        if (showLogoutDialog) {
            ConfirmLogoutDialog(
                onConfirm = {
                    showLogoutDialog = false
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun PerfilScreenCuidadorPreview() {
    SalusTheme {
        PerfilScreenCuidador(navController = rememberNavController())
    }
}