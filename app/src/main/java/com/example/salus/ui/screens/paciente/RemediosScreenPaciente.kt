package com.example.salus.ui.screens.paciente // Ajuste o pacote se necessário

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar // Importa a barra manual
import com.example.salus.ui.components.SalusVoiceFAB // Importa o FAB
import com.example.salus.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.salus.viewmodel.VoiceCommandViewModel
import androidx.compose.runtime.collectAsState

// --- Dados Simulados ---
data class RemedioSimulado(
    val nome: String,
    val icone: ImageVector,
    val corIcone: Color,
    val quantidade: Int,
    val apelido: String? = null,
    val dose: String,
    val frequencia: String
)
val remediosSimulados = listOf(
    RemedioSimulado("Remédio 1", Icons.Default.Medication, Color(0xFFFFC107), 4, apelido = "O da manhã", dose = "1 comprimido", frequencia = "A cada 24 horas"),
    RemedioSimulado("Remédio 2", Icons.Default.Medication, Color(0xFFE91E63), 10, dose = "2 comprimidos", frequencia = "De 12 em 12 horas"),
    RemedioSimulado("Remédio 3", Icons.Default.Medication, Color(0xFF16C565), 25, apelido = "Emergência", dose = "1 comprimido", frequencia = "Se necessário")
)
data class ConsultaSimulada(
    val data: String,
    val horario: String,
    val icone: ImageVector,
    val medico: String,
    val especialidade: String,
    val endereco: String
)
val consultaSimulada = ConsultaSimulada(
    data = "29 Setembro, 2024",
    horario = "08:00 AM - 10:30 AM",
    medico = "Dr. Richar Kandowian",
    especialidade = "Cardiologista",
    icone = Icons.Default.Person,
    endereco = "Rua Tito, 54, Vila Romana, São Paulo, SP"
)
// ------------------------------------


@Composable
fun RemediosScreenPaciente(
    navController: NavHostController, 
    voiceViewModel: VoiceCommandViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var remedioEmDetalhe by remember { mutableStateOf<RemedioSimulado?>(null) }
    val uriHandler = LocalUriHandler.current
    val isListening by voiceViewModel.isListening.collectAsState()

    // --- LAYOUT MANUAL COM BOX (Substituindo o Scaffold) ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // FundoClaro
    ) {

        // 1. Conteúdo do Ecrã (LazyColumn)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                // Adiciona padding em baixo para não ficar atrás da barra (90dp)
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Seção Remédios ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                remediosSimulados.forEach { remedio ->
                    RemedioCardPaciente( // <-- MUDANÇA: Chamando o card de Paciente
                        remedio = remedio,
                        onClick = {
                            remedioEmDetalhe = remedio // Mostra o diálogo de detalhes
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // --- Seção Consultas ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Consulta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                ConsultaCard(
                    consulta = consultaSimulada,
                    // <-- MUDANÇA: 3. Adicionar a ação de clique
                    onClick = {
                        // Formata o endereço e abre o Google Maps
                        val enderecoFormatado = consultaSimulada.endereco.replace(" ", "+")
                        uriHandler.openUri("https://maps.google.com/maps?q=$enderecoFormatado")
                    }
                )
            }

            // --- Seção Calendário Mini ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CalendarioMiniView(
                    modifier = Modifier.clickable {
                        navController.navigate(AppScreens.CalendarioPaciente.route) // <-- MUDANÇA: Rota correta
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // 2. A Barra de Navegação (MANUAL)
        SalusBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter), // Alinha na base
            currentRoute = currentRoute,
            onHomeClick = {
                navController.navigate(AppScreens.HomePaciente.route) { popUpTo(AppScreens.HomePaciente.route) { inclusive = true } }
            },
            onCalendarClick = { navController.navigate(AppScreens.CalendarioPaciente.route) },
            onNotificationsClick = { navController.navigate(AppScreens.NotificacoesPaciente.route) },
            onSettingsClick = { navController.navigate(AppScreens.PerfilPaciente.route) }
        )

        // 3. O Botão FAB (MANUAL)
        SalusVoiceFAB(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Alinha na base e centro
                // Puxa para cima metade da altura da barra (60dp / 2 = 30dp)
                .offset(y = (-20).dp),
            onClick = { voiceViewModel.startListening() },
            isListening = isListening
        )
        if (remedioEmDetalhe != null) {
            RemedioDetailsDialog(
                remedio = remedioEmDetalhe!!,
                onDismiss = { remedioEmDetalhe = null }
            )
        }
    }
}

// --- Componentes Internos (O seu código para RemedioCard, ConsultaCard, CalendarioMiniView) ---
// (Cole-os aqui exatamente como estavam no seu ficheiro anterior)

@Composable
private fun RemedioCardPaciente(
    remedio: RemedioSimulado,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // <-- MUDANÇA: Adicionado clique
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = remedio.icone,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = remedio.corIcone
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = remedio.nome,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.horizontalGradient(listOf(AzulGradienteInicio, AzulGradienteFim))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = remedio.quantidade.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ConsultaCard(
    consulta: ConsultaSimulada,
    onClick: () -> Unit // <-- MUDANÇA: Adicionado parâmetro onClick
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // <-- MUDANÇA: Card agora é clicável
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(AzulGradienteInicio, AzulGradienteFim)))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${consulta.data} | ${consulta.horario}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = consulta.icone,
                    contentDescription = "Avatar do Médico",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = consulta.medico,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = consulta.especialidade,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CinzaIcones)
        }
    }
}

@Composable
private fun CalendarioMiniView(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CalendarioFundo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabeçalho do Calendário
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Mês Anterior")
                Text(
                    text = "Set, 2025", // Data simulada
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Icon(Icons.Default.ChevronRight, contentDescription = "Próximo Mês")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Dias da Semana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("D", "S", "T", "Q", "Q", "S", "S").forEach { dia ->
                    Text(dia, fontWeight = FontWeight.SemiBold, color = CinzaIcones)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Dias (Simulação simples)
            Text("... (visualização dos dias do calendário) ...",
                modifier = Modifier.padding(32.dp).align(Alignment.CenterHorizontally),
                color = CinzaIcones,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RemedioDetailsDialog(
    remedio: RemedioSimulado,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(remedio.icone, contentDescription = null, tint = remedio.corIcone, modifier = Modifier.size(48.dp)) },
        title = {
            Text(
                text = remedio.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                remedio.apelido?.let {
                    Text(
                        text = "\"${it}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DetailRow(icon = Icons.Default.Medication, label = "Dose:", value = remedio.dose)
                DetailRow(icon = Icons.Default.AccessTime, label = "Frequência:", value = remedio.frequencia)
                DetailRow(icon = Icons.Default.Inventory, label = "Restantes:", value = "${remedio.quantidade} unidades")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun RemediosScreenPacientePreview() {
    SalusTheme {
        RemediosScreenPaciente(navController = rememberNavController())
    }
}