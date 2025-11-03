package com.example.salus.ui.screens.paciente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.ui.theme.*
import com.example.salus.ui.components.*

// --- Dados Simulados ---
private data class CuidadorSimulado(val id: String, val nome: String, val iconeRes: Int)
private val cuidadoresPendentes = listOf(
    CuidadorSimulado("id_cuidador_3", "Enfermeira Joana", R.drawable.icon_fem) // TODO: Adicionar avatares
)
private val cuidadoresAtivos = listOf(
    CuidadorSimulado("id_cuidador_1", "Carlos Almeida", R.drawable.icon_masc)
)
// -----------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerirCuidadoresScreen(navController: NavHostController) {

    // Estado para o diálogo de permissões
    var cuidadorSelecionado by remember { mutableStateOf<CuidadorSimulado?>(null) }
    // Estado para o diálogo de exclusão
    var cuidadorParaExcluir by remember { mutableStateOf<CuidadorSimulado?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerir Cuidadores") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        // TODO: Adicionar um FAB (+) para o Paciente adicionar Cuidadores

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            // --- Caixa de Solicitações Pendentes ---
            item {
                Text(
                    text = "Solicitações Pendentes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            items(cuidadoresPendentes) { cuidador ->
                PendingCaregiverCard(
                    cuidador = cuidador,
                    onAccept = { /* TODO: Lógica de aceitar */ },
                    onDecline = { /* TODO: Lógica de recusar */ }
                )
            }

            // --- Lista de Cuidadores Ativos ---
            item {
                Text(
                    text = "Meus Cuidadores",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            items(cuidadoresAtivos) { cuidador ->
                ActiveCaregiverCard(
                    cuidador = cuidador,
                    onClick = { cuidadorSelecionado = cuidador } // Abre o pop-up de permissões
                )
            }
        }

        // --- Diálogo de Permissões ---
        if (cuidadorSelecionado != null) {
            PermissionDialog(
                cuidadorNome = cuidadorSelecionado!!.nome,
                onDismiss = { cuidadorSelecionado = null },
                onRemoveClick = {
                    // Fecha o diálogo de permissões e abre o de exclusão
                    cuidadorParaExcluir = cuidadorSelecionado
                    cuidadorSelecionado = null
                }
            )
        }

        // --- Diálogo de Confirmação de Exclusão ---
        if (cuidadorParaExcluir != null) {
            // Reutilizando o ConfirmLogoutDialog
            AlertDialog(
                onDismissRequest = { cuidadorParaExcluir = null },
                icon = { Icon(Icons.Default.Warning, "Aviso") },
                title = { Text("Excluir Cuidador") },
                text = { Text("Tem a certeza que deseja remover '${cuidadorParaExcluir!!.nome}' da sua lista de cuidadores?") },
                confirmButton = {
                    Button(
                        onClick = { /* TODO: Lógica de exclusão */ ; cuidadorParaExcluir = null },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Excluir") }
                },
                dismissButton = {
                    TextButton(onClick = { cuidadorParaExcluir = null }) { Text("Cancelar") }
                }
            )
        }
    }
}

// --- Componentes Privados ---

@Composable
private fun ActiveCaregiverCard(
    cuidador: CuidadorSimulado,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = cuidador.iconeRes),
                contentDescription = "Avatar",
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = cuidador.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.Settings, "Configurações", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PendingCaregiverCard(
    cuidador: CuidadorSimulado,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Cor diferente
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = cuidador.iconeRes),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = cuidador.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = BotaoAdicionar)
                ) {
                    Text("Aceitar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onDecline) {
                    Text("Recusar")
                }
            }
        }
    }
}

@Composable
private fun PermissionDialog(
    cuidadorNome: String,
    onDismiss: () -> Unit,
    onRemoveClick: () -> Unit
) {
    var permissaoLocalizacao by remember { mutableStateOf(true) }
    var permissaoRemedios by remember { mutableStateOf(true) }
    var permissaoAlertas by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Settings, "Permissões") },
        title = { Text("Permissões de '$cuidadorNome'") },
        text = {
            Column {
                NotificationToggle( // Reutilizando o componente
                    text = "Ver Localização",
                    checked = permissaoLocalizacao,
                    onCheckedChange = { permissaoLocalizacao = it }
                )
                NotificationToggle(
                    text = "Gerir Remédios",
                    checked = permissaoRemedios,
                    onCheckedChange = { permissaoRemedios = it }
                )
                NotificationToggle(
                    text = "Receber Alertas de AVC",
                    checked = permissaoAlertas,
                    onCheckedChange = { permissaoAlertas = it }
                )
            }
        },
        confirmButton = {
            // Botão "Remover" dentro do pop-up
            Button(
                onClick = onRemoveClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remover Cuidador")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun GerirCuidadoresScreenPreview() {
    SalusTheme {
        GerirCuidadoresScreen(navController = rememberNavController())
    }
}