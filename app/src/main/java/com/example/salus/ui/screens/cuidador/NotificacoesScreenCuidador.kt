package com.example.salus.ui.screens.cuidador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Warning
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

// --- Dados Simulados Cuidador ---
private enum class CategoriaCuidador { TODAS, ALERTAS_AVC, LEMBRETES }
private data class NotificacaoCuidador(
    val id: Int,
    val icone: ImageVector,
    val titulo: String,
    val timestamp: String,
    val categoria: CategoriaCuidador,
    val rotaDestino: String
)
private val notificacoesCuidadorSimuladas = listOf(
    NotificacaoCuidador(1, Icons.Default.Warning, "ALERTA DE AVC: Carlos Almeida", "Agora", CategoriaCuidador.ALERTAS_AVC, AppScreens.AlertaRecebidoCuidador.route),
    NotificacaoCuidador(2, Icons.Default.Inventory, "Baixo stock de 'Remédio 2' para Maria Joaquina", "Há 1h", CategoriaCuidador.LEMBRETES, AppScreens.RemediosCuidador.route),
    NotificacaoCuidador(3, Icons.Default.Warning, "ALERTA DE AVC: Maria Joaquina", "Há 5h", CategoriaCuidador.ALERTAS_AVC, AppScreens.AlertaRecebidoCuidador.route)
)
// ---------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacoesScreenCuidador(navController: NavHostController) {

    var categoriaSelecionada by remember { mutableStateOf(CategoriaCuidador.TODAS) }

    val notificacoesFiltradas = remember(categoriaSelecionada) {
        if (categoriaSelecionada == CategoriaCuidador.TODAS) {
            notificacoesCuidadorSimuladas
        } else {
            notificacoesCuidadorSimuladas.filter { it.categoria == categoriaSelecionada }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificações") },
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
        containerColor = MaterialTheme.colorScheme.background
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
                        selected = categoriaSelecionada == CategoriaCuidador.TODAS,
                        onClick = { categoriaSelecionada = CategoriaCuidador.TODAS },
                        label = { Text("Todas") },
                        leadingIcon = if (categoriaSelecionada == CategoriaCuidador.TODAS) { { Icon(Icons.Default.Check, "Selecionado") } } else { null }
                    )
                }
                item {
                    FilterChip(
                        selected = categoriaSelecionada == CategoriaCuidador.ALERTAS_AVC,
                        onClick = { categoriaSelecionada = CategoriaCuidador.ALERTAS_AVC },
                        label = { Text("Alertas de AVC") },
                        leadingIcon = if (categoriaSelecionada == CategoriaCuidador.ALERTAS_AVC) { { Icon(Icons.Default.Check, "Selecionado") } } else { null }
                    )
                }
                item {
                    FilterChip(
                        selected = categoriaSelecionada == CategoriaCuidador.LEMBRETES,
                        onClick = { categoriaSelecionada = CategoriaCuidador.LEMBRETES },
                        label = { Text("Lembretes de Pacientes") },
                        leadingIcon = if (categoriaSelecionada == CategoriaCuidador.LEMBRETES) { { Icon(Icons.Default.Check, "Selecionado") } } else { null }
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
                            // TODO: Passar o ID do paciente/alerta se necessário
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

// --- Componente Privado (Copie-o do Paciente) ---
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
            .clickable(onClick = onClick),
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
                // MUDANÇA: Cor diferente para Alertas de AVC
                tint = if (icone == Icons.Default.Warning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
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
fun NotificacoesScreenCuidadorPreview() {
    SalusTheme {
        NotificacoesScreenCuidador(navController = rememberNavController())
    }
}