package com.example.salus.ui.screens.paciente // Ajuste o pacote se necessário

// --- NOVOS IMPORTS DO GOOGLE MAPS ---
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
// ------------------------------------

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar
import com.example.salus.ui.components.SalusVoiceFAB
import com.example.salus.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.salus.viewmodel.VoiceCommandViewModel
import androidx.compose.runtime.collectAsState

// --- MUDANÇA: Dados Simulados agora incluem LatLng ---
data class LocalSimulado(
    val nome: String,
    val endereco: String,
    val icone: ImageVector,
    val corIcone: Color,
    val latLng: LatLng // Coordenada do local
)
// Coordenadas simuladas perto da Vila Romana / Lapa (onde estava o seu pino)
val hospitaisSimulados = listOf(
    LocalSimulado("Hospital Metropolitano", "R. Marcelina, 441 - Vila Romana", Icons.Default.LocalHospital, VermelhoGradienteInicio, LatLng(-23.5303, -46.6908)),
    LocalSimulado("Hospital Albert Sabin", "Rua Brigadeiro Gavião Peixoto, 123", Icons.Default.LocalHospital, VermelhoGradienteInicio, LatLng(-23.5350, -46.6965))
)
val farmaciasSimuladas = listOf(
    LocalSimulado("Drogaria São Paulo", "R. Tito, 63 - Vila Romana", Icons.Default.LocationOn, AzulGradienteFim, LatLng(-23.5325, -46.6917)),
    LocalSimulado("Droga Raia", "R. Clélia, 900 - Lapa", Icons.Default.LocationOn, AzulGradienteFim, LatLng(-23.5298, -46.6899))
)
// Ponto central para o mapa
val localizacaoPacienteSimulada = LatLng(-23.5320, -46.6915) // Ponto médio
// ------------------------------------

@Composable
fun MapaScreenPaciente(
    navController: NavHostController,
    voiceViewModel: VoiceCommandViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isListening by voiceViewModel.isListening.collectAsState()

    // --- Estado da Câmera do Mapa ---
    // Inicia a câmera centrada na localização simulada do paciente
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(localizacaoPacienteSimulada, 15f) // Zoom 15f (nível de bairro)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Item 1: Google Map (Substitui a Imagem) ---
            item {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp), // Altura fixa para o mapa
                    cameraPositionState = cameraPositionState
                ) {
                    // Adiciona um pino para o paciente (opcional)
                    Marker(
                        state = MarkerState(position = localizacaoPacienteSimulada),
                        title = "Sua Localização"
                        // TODO: Adicionar ícone customizado para o paciente
                    )
                    // Adiciona pinos para os hospitais
                    hospitaisSimulados.forEach { local ->
                        Marker(
                            state = MarkerState(position = local.latLng),
                            title = local.nome,
                            snippet = local.endereco
                        )
                    }
                    // Adiciona pinos para as farmácias
                    farmaciasSimuladas.forEach { local ->
                        Marker(
                            state = MarkerState(position = local.latLng),
                            title = local.nome,
                            snippet = local.endereco
                        )
                    }
                }
            }

            // --- Item 2: Título Hospitais ---
            item {
                Text(
                    text = "Hospitais Próximos",
                    // ... (resto do Text)
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp, bottom = 8.dp)
                )
            }

            // --- Item 3: Lista de Hospitais (Não muda) ---
            items(hospitaisSimulados) { local ->
                PlaceCard(local = local, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(12.dp))
            }

            // --- Item 4: Título Farmácias ---
            item {
                Text(
                    text = "Farmácias Próximas",
                    // ... (resto do Text)
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }

            // --- Item 5: Lista de Farmácias (Não muda) ---
            items(farmaciasSimuladas) { local ->
                PlaceCard(local = local, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(12.dp))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // --- Barra de Navegação e FAB (Não mudam) ---
        SalusBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentRoute = currentRoute,
            onHomeClick = { navController.navigate(AppScreens.HomePaciente.route) },
            onCalendarClick = { navController.navigate(AppScreens.CalendarioPaciente.route) },
            onNotificationsClick = { navController.navigate(AppScreens.NotificacoesPaciente.route) },
            onSettingsClick = { navController.navigate(AppScreens.PerfilPaciente.route) }
        )
        SalusVoiceFAB(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            onClick = { voiceViewModel.startListening() },
            isListening = isListening
        )
    }
}

// --- Componente de Cartão de Local ---
@Composable
private fun PlaceCard(local: LocalSimulado, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // CardBranco
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = local.icone,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = local.corIcone
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = local.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = local.endereco,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
