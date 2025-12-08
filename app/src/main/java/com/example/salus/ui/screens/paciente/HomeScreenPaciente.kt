package com.example.salus.ui.screens.paciente


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Mic
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar // <-- Importa o componente
import com.example.salus.ui.theme.*
import com.example.salus.viewmodel.HomePacienteViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.salus.domain.AlertManager
import com.example.salus.ui.components.SalusVoiceFAB
import com.example.salus.viewmodel.VoiceCommandViewModel

@Composable
fun HomeScreenPaciente(
    navController: NavHostController,
    viewModel: HomePacienteViewModel = hiltViewModel(),
    voiceViewModel: VoiceCommandViewModel = hiltViewModel()
) {
    val nomeUsuario by viewModel.nomeUsuario.collectAsState()
    val bpmSimulado by viewModel.bpmSimulado.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = navBackStackEntry?.destination?.route
    val currentRoute = AppScreens.HomePaciente.route
    val context = LocalContext.current
    val isListening by voiceViewModel.isListening.collectAsState()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // FundoClaro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                // Adiciona padding em baixo para o conteúdo não ficar
                // escondido atrás da barra (60dp da barra + 24dp de espaço)
                .padding(bottom = 84.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HeaderPaciente(nomeUsuario = nomeUsuario)
            Spacer(modifier = Modifier.height(30.dp))
            BotaoAlerta(onClick = { AlertManager.sendEmergencySms(context) })
            Spacer(modifier = Modifier.height(30.dp))
            VisorBPM(bpm = bpmSimulado,
                modifier = Modifier.clickable{
                    navController.navigate(AppScreens.FASTScreen.route)
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BotaoNavegacaoPequeno(
                    texto = "Remédios",
                    icone = Icons.Default.Medication,
                    onClick = { navController.navigate(AppScreens.RemediosPaciente.route) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                BotaoNavegacaoPequeno(
                    texto = "Mapa",
                    icone = Icons.Default.GpsFixed,
                    onClick = { navController.navigate(AppScreens.MapaPaciente.route) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            BotaoChatbot(onClick = { navController.navigate(AppScreens.Chatbot.route) })
            Spacer(modifier = Modifier.height(24.dp))
        }


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

        // 4. O Botão FAB (MANUAL)
        // Alinhamos na base e centro do Box (fica por cima de tudo)
        SalusVoiceFAB(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            onClick = {
                voiceViewModel.startListening()
            },
            isListening = isListening
        )
    }
}

// --- Cole aqui os seus Componentes Internos Privados ---
// (HeaderPaciente, BotaoAlerta, VisorBPM, BotaoNavegacaoPequeno, BotaoChatbot)
// ...
@Composable
private fun HeaderPaciente(nomeUsuario: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Olá, seja bem-vindo",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = nomeUsuario,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun BotaoAlerta(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
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
                painter = painterResource(id = R.drawable.sirene),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
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
private fun VisorBPM(bpm: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(125.dp)
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
                painter = painterResource(id = R.drawable.batimentos),
                contentDescription = null,
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.Fit,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$bpm",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 1.em
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
            .height(120.dp),
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
                painter = painterResource(id = R.drawable.sia),
                contentDescription = "Avatar SalusAI",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}


// --- Preview (Atualizado) ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomeScreenPacientePreview() {
    SalusTheme {
        val fakeNavController = rememberNavController()
        HomeScreenPaciente(navController = fakeNavController)
    }
}
