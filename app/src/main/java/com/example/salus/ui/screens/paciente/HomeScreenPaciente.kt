package com.example.salus.ui.screens.paciente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed // Ícone para Mapa
import androidx.compose.material.icons.filled.Medication // Ícone para Remédios
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.salus.R // Import R
import com.example.salus.ui.theme.* // Importe suas cores
import com.example.salus.ui.components.SalusBottomBar
import com.example.salus.ui.components.SalusVoiceFAB
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FabPosition
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.salus.navigation.AppScreens

@Composable
fun HomeScreenPaciente(navController: NavHostController) {
    // --- Dados Simulados para o MVP ---
    // Simular o nome do usuário (depois virá do ViewModel)
    val nomeUsuario = "Mateus Kenji"

    // Simular o BPM aleatório entre 68 e 75
    var bpmSimulado by remember { mutableStateOf((68..75).random()) }
    // TODO: Podemos adicionar um LaunchedEffect com delay para atualizar o BPM de tempos em tempos

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        floatingActionButton = {
            SalusVoiceFAB(onClick = { /* TODO: Lógica de comando de voz */ })
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            SalusBottomBar(
                currentRoute = currentRoute, // Passa a rota atual
                onHomeClick = {
                    navController.navigate(AppScreens.HomePaciente.route) { popUpTo(AppScreens.HomePaciente.route) { inclusive = true } }
                },
                onCalendarClick = { navController.navigate(AppScreens.Calendario.route) },
                onNotificationsClick = { navController.navigate(AppScreens.Notificacoes.route) },
                onProfileClick = { navController.navigate(AppScreens.Perfil.route) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background // FundoClaro
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // Permite rolar a tela
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. Header de Boas Vindas
            HeaderPaciente(nomeUsuario = nomeUsuario)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Botão de Alerta
            BotaoAlerta(onClick = { /* TODO: Lógica de Alerta */ })

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Visor BPM
            VisorBPM(bpm = bpmSimulado)

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Botões Remédios e Mapa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BotaoNavegacaoPequeno(
                    texto = "Remédios",
                    icone = Icons.Default.Medication,
                    onClick = { /* TODO: navController.navigate(AppScreens.RemediosPaciente.route) */ },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                BotaoNavegacaoPequeno(
                    texto = "Mapa",
                    icone = Icons.Default.GpsFixed,
                    onClick = { /* TODO: navController.navigate(AppScreens.MapaPaciente.route) */ },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Botão Chatbot
            BotaoChatbot(onClick = { /* TODO: navController.navigate(AppScreens.Chatbot.route) */ })

            Spacer(modifier = Modifier.height(24.dp)) // Espaço no final
        }
    }
}

// --- Componentes Internos da HomeScreenPaciente ---

@Composable
private fun HeaderPaciente(nomeUsuario: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Olá, seja bem-vindo",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant // TextoSecundarioCinza
        )
        Text(
            text = nomeUsuario,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground // TextoPrimarioClaro
        )
    }
}

@Composable
private fun BotaoAlerta(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(VermelhoGradienteInicio, VermelhoGradienteFim)
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sirene), // TODO: Adicionar ícone de sirene
                contentDescription = null,
                modifier = Modifier.size(50.dp) // Ajuste
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Alertar!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun VisorBPM(bpm: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(AzulGradienteInicio, AzulGradienteFim)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.batimentos), // TODO: Adicionar ícone de batimento
                contentDescription = null,
                modifier = Modifier.height(50.dp), // Ajuste
                contentScale = ContentScale.Fit,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$bpm",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 1.em // Ajusta altura da linha para alinhar melhor
                )
                Text(
                    text = "bpm",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun BotaoNavegacaoPequeno(
    texto: String,
    icone: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // CardBranco
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(imageVector = icone, contentDescription = null, tint = AzulGradienteInicio)
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                color = AzulGradienteFim
            )
        }
    }
}

@Composable
private fun BotaoChatbot(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SalusAI",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AzulGradienteFim
            )
            Image(
                painter = painterResource(id = R.drawable.sia), // TODO: Adicionar avatar da SIA
                contentDescription = "Avatar SalusAI",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}
