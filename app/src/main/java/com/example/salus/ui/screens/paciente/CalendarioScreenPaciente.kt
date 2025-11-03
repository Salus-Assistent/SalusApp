package com.example.salus.ui.screens.paciente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// --- Dados Simulados para o Calendário ---
// (Numa app real, isto viria do ViewModel/Repositório)
data class EventoCalendario(
    val data: LocalDate,
    val titulo: String,
    val icone: ImageVector
)
val eventosSimulados = listOf(
    EventoCalendario(LocalDate.now(), "Tomar Remédio 1", Icons.Default.Medication),
    EventoCalendario(LocalDate.now(), "Consulta Dr. Kandowian", Icons.Default.Person),
    EventoCalendario(LocalDate.now().plusDays(2), "Tomar Remédio 2", Icons.Default.Medication),
    EventoCalendario(LocalDate.now().plusDays(2), "Tomar Remédio 3", Icons.Default.Medication)
)
// ------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreenPaciente(navController: NavHostController) {

    // Estado para o DatePicker
    val datePickerState = rememberDatePickerState()

    // Converte a data selecionada (em milissegundos) para LocalDate
    val dataSelecionada: LocalDate = datePickerState.selectedDateMillis?.let { millis ->
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    } ?: LocalDate.now() // Se nada for selecionado, usa hoje

    // Filtra a lista de eventos com base na data selecionada
    val eventosDoDia = remember(dataSelecionada) {
        eventosSimulados.filter { it.data == dataSelecionada }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendário") },
                // Botão "Voltar" no canto superior esquerdo
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
        // SEM BottomBar
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // --- Parte 1: O Calendário ---
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(horizontal = 16.dp),
                title = null, // Remove o título "Select date"
                headline = null, // Remove o "Mon, May 15"
                showModeToggle = false, // Remove o botão de alternar para entrada de texto
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    selectedDayContainerColor = AzulGradienteFim,
                    todayDateBorderColor = AzulGradienteFim
                )
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))

            // --- Parte 2: A Lista de Eventos Filtrada ---
            Text(
                text = "Eventos do dia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (eventosDoDia.isEmpty()) {
                Text(
                    text = "Nenhum evento para este dia.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = CinzaIcones
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(eventosDoDia) { evento ->
                        EventoCard(evento = evento)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

// --- Componente Privado para o Evento ---
@Composable
private fun EventoCard(evento: EventoCalendario) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = evento.icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = evento.titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CalendarioScreenPacientePreview() {
    SalusTheme {
        CalendarioScreenPaciente(navController = rememberNavController())
    }
}