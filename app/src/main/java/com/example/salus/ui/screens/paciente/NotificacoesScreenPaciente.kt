package com.example.salus.ui.screens.paciente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.theme.*

// --- Dados Simulados Paciente ---
private enum class CategoriaPaciente { TODAS, REMEDIOS, CONSULTAS }
private data class NotificacaoPaciente(
    val id: Int,
    val icone: ImageVector,
    val titulo: String,
    val timestamp: String,
    val categoria: CategoriaPaciente,
    val rotaDestino: String // Para a Opção B
)
private val notificacoesPacienteSimuladas = listOf(
    NotificacaoPaciente(1, Icons.Default.Medication, "Hora de tomar Remédio 1", "Agora", CategoriaPaciente.REMEDIOS, AppScreens.RemediosPaciente.route),
    NotificacaoPaciente(2, Icons.Default.Person, "Consulta com Dr. Richar amanhã", "Há 1h", CategoriaPaciente.CONSULTAS, AppScreens.CalendarioPaciente.route),
    NotificacaoPaciente(3, Icons.Default.Medication, "Hora de tomar Remédio 2", "Há 3h", CategoriaPaciente.REMEDIOS, AppScreens.RemediosPaciente.route)
)
// ---------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacoesScreenPaciente(navController: NavHostController) {

    // Estado para o filtro de "classe"
    var categoriaSelecionada by remember { mutableStateOf(CategoriaPaciente.TODAS) }

    // Filtra a lista de notificações com base no estado
    val notificacoesFiltradas = remember(categoriaSelecionada) {
        if (categoriaSelecionada == CategoriaPaciente.TODAS) {
            notificacoesPacienteSimuladas
        } else {
            notificacoesPacienteSimuladas.filter { it.categoria == categoriaSelecionada }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificações") },
                // Botão "Voltar"
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        // SEM BottomBar
        containerColor = MaterialTheme.colorScheme.background // FundoClaro
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // --- Filtros (Chips) ---
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = categoriaSelecionada == CategoriaPaciente.TODAS,
                        onClick = { categoriaSelecionada = CategoriaPaciente.TODAS },
                        label = { Text("Todas") },
                        leadingIcon = if (categoriaSelecionada == CategoriaPaciente.TODAS) { { Icon(Icons.Default.Check, "Selecionado") } } else { null }
                    )
                }
                item {
                    FilterChip(
                        selected = categoriaSelecionada == CategoriaPaciente.REMEDIOS,
                        onClick = { categoriaSelecionada = CategoriaPaciente.REMEDIOS },
                        label = { Text("Lembretes de Remédios") },
                        leadingIcon = if (categoriaSelecionada == CategoriaPaciente.REMEDIOS) { { Icon(Icons.Default.Check, "Selecionado") } } else { null }
                    )
                }
                item {
                    FilterChip(
                        selected = categoriaSelecionada == CategoriaPaciente.CONSULTAS,
                        onClick = { categoriaSelecionada = CategoriaPaciente.CONSULTAS },
                        label = { Text("Consultas Agendadas") },
                        leadingIcon = if (categoriaSelecionada == CategoriaPaciente.CONSULTAS) { { Icon(Icons.Default.Check, "Selecionado") } } else { null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // --- Lista de Notificações ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notificacoesFiltradas) { notificacao ->
                    NotificationItemCard(
                        icone = notificacao.icone,
                        titulo = notificacao.titulo,
                        timestamp = notificacao.timestamp,
                        onClick = {
                            // Opção B: Navegar para o ecrã relevante
                            navController.navigate(notificacao.rotaDestino)
                        }
                    )
                }
                if (notificacoesFiltradas.isEmpty()) {
                    item {
                        Text(
                            "Nenhuma notificação nesta categoria.",
                            color = CinzaIcones,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// --- Componente Privado para o Item da Lista ---
@Composable
private fun NotificationItemCard(
    icone: ImageVector,
    titulo: String,
    timestamp: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Clicável
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun NotificacoesScreenPacientePreview() {
    SalusTheme {
        NotificacoesScreenPaciente(navController = rememberNavController())
    }
}