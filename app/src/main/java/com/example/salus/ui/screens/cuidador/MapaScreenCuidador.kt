package com.example.salus.ui.screens.cuidador // Ajuste o pacote se necessário

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.salus.R
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar
import com.example.salus.ui.components.SalusVoiceFAB
import com.example.salus.ui.theme.*
import com.example.salus.viewmodel.CuidadorSharedViewModel
import com.example.salus.viewmodel.VoiceCommandViewModel

// Ponto central para o mapa (mesmo do paciente)
val localizacaoPacienteSimulada = LatLng(-23.528113252597624, -46.691858318684694)

@Composable
fun MapaScreenCuidador(
    navController: NavHostController, 
    sharedViewModel: CuidadorSharedViewModel,
    voiceViewModel: VoiceCommandViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isListening by voiceViewModel.isListening.collectAsState()

    // --- Dados Simulados ---
    val bpmSimuladoPaciente = 72
    val nomePaciente = "Carlos Almeida" // Exemplo

    // --- Estado da Câmera do Mapa ---
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(localizacaoPacienteSimulada, 16f) // Zoom 16f (mais próximo)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 1. Google Map em tela cheia
        GoogleMap(
            modifier = Modifier.fillMaxSize(), // Mapa ocupa TUDO
            cameraPositionState = cameraPositionState
            // TODO: Adicionar propriedades do mapa (uiSettings) se quiser desativar zoom/scroll
        ) {
            // Adiciona o pino estático do paciente
            Marker(
                state = MarkerState(position = localizacaoPacienteSimulada),
                title = "Localização de $nomePaciente"
                // TODO: Adicionar ícone customizado (foto do paciente?)
            )
        }

        // 2. Barra de BPM do Paciente (Sobreposta)
        PacienteBpmBar(
            bpm = bpmSimuladoPaciente,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
                .padding(horizontal = 16.dp)
        )

        // 3. A Barra de Navegação (MANUAL)
        SalusBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentRoute = currentRoute,
            onHomeClick = { navController.navigate(AppScreens.HomeCuidador.route) },
            onCalendarClick = { navController.navigate(AppScreens.CalendarioCuidador.route) },
            onNotificationsClick = { navController.navigate(AppScreens.NotificacoesCuidador.route) },
            onSettingsClick = { navController.navigate(AppScreens.PerfilCuidador.route) }
        )

        // 4. O Botão FAB (MANUAL)
        SalusVoiceFAB(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-10).dp),
            onClick = { voiceViewModel.startListening() },
            isListening = isListening
        )
    }
}

// --- Componente Privado para a Barra de BPM ---
@Composable
private fun PacienteBpmBar(bpm: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp))
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
                contentDescription = "BPM Atual",
                modifier = Modifier.height(150.dp).size(50.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White)
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$bpm",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 1.em
                )
                Text(
                    text = "bpm",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
            }
        }
    }
}


