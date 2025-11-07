package com.example.salus.ui.screens.paciente

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
import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class) // Necessário para o TopAppBar
@Composable
fun PerfilScreenPaciente(navController: NavHostController) {

    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var remediosNotif by remember { mutableStateOf(true) }
    var bpmNotif by remember { mutableStateOf(false) }

    // Usamos Scaffold para o layout de ecrã padrão com TopAppBar
    Scaffold(
        topBar = {
            // A nova barra no topo
            TopAppBar(
                title = { /* Vazio, como pedido */ }, // <-- TÍTULO REMOVIDO
                navigationIcon = {
                    // O botão "Voltar"
                    IconButton(onClick = { navController.popBackStack() }) { // Navega para o ecrã anterior
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // <-- FUNDO TRANSPARENTE
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground // Cor do ícone
                )
            )
        },
        // REMOVEMOS a bottomBar e o floatingActionButton

        containerColor = MaterialTheme.colorScheme.background // FundoClaro
    ) { innerPadding ->

        // Conteúdo do Ecrã
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplica o padding do Scaffold
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Spacer(modifier = Modifier.height(40.dp)) // O TopAppBar já dá algum espaço

            // Avatar e Nome
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mateus Kenji", // Nome Simulado
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Cartão de Opções
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {

                    ProfileOptionButton(
                        text = "Editar Perfil",
                        icon = Icons.Default.Edit,
                        onClick = { navController.navigate(AppScreens.EditarPerfil.route) } //
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileOptionButton(
                        text = "Gerir Cuidadores",
                        icon = Icons.Default.Group,
                        onClick = { navController.navigate(AppScreens.GerirCuidadores.route) } //
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
                            text = "Lembretes de Remédios",
                            checked = remediosNotif,
                            onCheckedChange = { remediosNotif = it }
                        )
                        NotificationToggle(
                            text = "Alertas de BPM",
                            checked = bpmNotif,
                            onCheckedChange = { bpmNotif = it }
                        )
                    }

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileOptionButton(
                        text = "Ajuda & Tutorial",
                        icon = Icons.Default.HelpOutline,
                        onClick = { navController.navigate(AppScreens.AjudaTutorial.route) } //
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileOptionButton(
                        text = "Privacidade & Termos",
                        icon = Icons.Default.Lock,
                        onClick = { navController.navigate(AppScreens.PrivacidadeTermos.route) } //
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileOptionButton(
                        text = "Ativar Atalho de Emergência",
                        icon = Icons.Default.VolumeUp,
                        onClick = {
                            // Abre as definições de Acessibilidade do telemóvel
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            context.startActivity(intent)
                        }
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
fun PerfilScreenPacientePreview() {
    SalusTheme {
        PerfilScreenPaciente(navController = rememberNavController())
    }
}