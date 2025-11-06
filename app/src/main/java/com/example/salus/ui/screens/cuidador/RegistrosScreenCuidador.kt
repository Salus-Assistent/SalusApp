package com.example.salus.ui.screens.cuidador // Ajuste o pacote se necessário

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.salus.R
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar
import com.example.salus.ui.components.SalusVoiceFAB
import com.example.salus.ui.theme.AzulGradienteFim
import com.example.salus.ui.theme.BotaoAdicionar
import com.example.salus.ui.theme.CinzaIcones
import com.example.salus.ui.theme.GraficoDiaNaoSelecionado
import com.example.salus.ui.theme.GraficoDiaSelecionado
import com.example.salus.viewmodel.CuidadorSharedViewModel
import kotlin.random.Random
import com.example.salus.data.model.PacienteSimulado

// --- Dados Simulados para o Gráfico e Estatísticas ---
// --- Dados Simulados (Atualizados) ---
data class BpmDiario(val dia: String, val valor: Int)

private fun gerarDadosMensais(destaqueDia: String, valorDestaque: Int): List<BpmDiario> {
    return (1..31).map { dia ->
        if (dia.toString() == destaqueDia) {
            BpmDiario(dia.toString(), valorDestaque)
        } else {
            BpmDiario(dia.toString(), Random.nextInt(60, 91)) // Valores aleatórios entre 60 e 90
        }
    }
}
val dadosOutubro = gerarDadosMensais(destaqueDia = "14", valorDestaque = 62) // Dia 14 com 62
val dadosSetembro = gerarDadosMensais(destaqueDia = "16", valorDestaque = 65)

data class StatsBPM(val media: Int, val maior: Int, val menor: Int)
val statsOutubro = StatsBPM(65, 85, 61)
val statsSetembro = StatsBPM(70, 90, 58)


@Composable
fun RegistrosScreenCuidador(
    navController: NavHostController,
    sharedViewModel: CuidadorSharedViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para o seletor de mês
    var mesAtual by remember { mutableStateOf("Outubro 2025") }
    var dadosGrafico by remember { mutableStateOf(dadosOutubro) }
    var dadosStats by remember { mutableStateOf(statsOutubro) }
    var showAddPacienteDialog by remember { mutableStateOf(false) }
    var diaSelecionado by remember { mutableStateOf("14") }

    // Estado para o pop-up de adicionar paciente
    val pacienteSelecionado by sharedViewModel.pacienteSelecionado.collectAsState()
    var pacienteParaConfirmar by remember { mutableStateOf<PacienteSimulado?>(null) }

    var pacienteParaExcluir by remember { mutableStateOf<PacienteSimulado?>(null) }
    val pacientesList by sharedViewModel.pacientesDisponiveis.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // FundoClaro
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp), // Padding para a barra
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Card do Calendário/Gráfico ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Seletor de Mês (com lógica simulada)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                mesAtual = "Setembro 2025"; dadosGrafico = dadosSetembro; dadosStats = statsSetembro; diaSelecionado = "16" // Seleciona o dia de destaque do outro mês
                            }) { Icon(Icons.Default.ChevronLeft, "Mês Anterior") }

                            Text(mesAtual, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                            IconButton(onClick = {
                                mesAtual = "Outubro 2025"; dadosGrafico = dadosOutubro; dadosStats = statsOutubro; diaSelecionado = "14" // Seleciona o dia de destaque
                            }) { Icon(Icons.Default.ChevronRight, "Próximo Mês") }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        // Gráfico de Barras Atualizado
                        GraficoBPM(
                            dados = dadosGrafico,
                            diaSelecionado = diaSelecionado,
                            onDiaClick = { dia ->
                                diaSelecionado = dia // Atualiza o estado do dia clicado
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        StatsBPMView(stats = dadosStats)
                    }
                }
            }

            // --- Título da Lista de Pacientes ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Pacientes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // --- Lista de Pacientes ---
            items(pacientesList) { paciente -> // <-- MUDANÇA: Usa a lista do VM
                key(paciente.id) {
                    PacienteCard(
                        paciente = paciente,
                        isSelected = paciente.id == pacienteSelecionado?.id,
                        onClick = {
                            pacienteParaConfirmar = paciente
                        },
                        onDelete = {
                            pacienteParaExcluir = paciente
                        }
                    )
                }
            }

            // --- Botão Adicionar Paciente ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showAddPacienteDialog = true }, // Abre o pop-up de UUID
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BotaoAdicionar) // Cor verde
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Paciente", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Adicionar Paciente")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Espaço final
            }


        } // Fim da LazyColumn

        // --- Barra de Navegação e FAB (Não mudam) ---
        SalusBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentRoute = currentRoute,
            onHomeClick = { navController.navigate(AppScreens.HomeCuidador.route) },
            onCalendarClick = { navController.navigate(AppScreens.CalendarioCuidador.route) },
            onNotificationsClick = { navController.navigate(AppScreens.NotificacoesCuidador.route) },
            onSettingsClick = { navController.navigate(AppScreens.PerfilCuidador.route) }
        )
        SalusVoiceFAB(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp),
            onClick = { /* TODO */ }
        )

        // --- Diálogos ---
        if (showAddPacienteDialog) {
            AddPacienteDialog(
                onDismiss = { showAddPacienteDialog = false },
                onConfirm = { uuid ->
                    // MUDANÇA: Chama o ViewModel para adicionar
                    sharedViewModel.adicionarPaciente(uuid)
                    showAddPacienteDialog = false
                }
            )
        }

        if (pacienteParaConfirmar != null) {
            ConfirmarSelecaoPacienteDialog(
                pacienteNome = pacienteParaConfirmar!!.nome,
                onConfirm = {
                    // MUDANÇA: Chama o ViewModel para selecionar
                    sharedViewModel.selecionarPaciente(pacienteParaConfirmar!!)
                    pacienteParaConfirmar = null
                },
                onDismiss = { pacienteParaConfirmar = null }
            )
        }

        if (pacienteParaExcluir != null) {
            ConfirmDeleteDialog(
                itemName = pacienteParaExcluir!!.nome,
                onConfirm = {
                    // MUDANÇA: Chama o ViewModel para remover
                    sharedViewModel.removerPaciente(pacienteParaExcluir!!)
                    pacienteParaExcluir = null
                },
                onDismiss = { pacienteParaExcluir = null }
            )
        }

    }
}

// --- Componentes Privados para RegistrosScreenCuidador ---

@Composable
private fun GraficoBPM(
    dados: List<BpmDiario>,
    diaSelecionado: String,
    onDiaClick: (String) -> Unit
) {
    // 1. Encontra o valor máximo nos dados para calcular a altura relativa (ex: 90)
    val maxValor = dados.maxOfOrNull { it.valor }?.toFloat() ?: 1f // Evita divisão por zero

    // 2. Define alturas fixas para os componentes do gráfico
    val alturaMaximaBarra = 100.dp // Altura máxima que a barra mais alta (ex: 90) pode ter
    val alturaLabelValor = 22.dp // Espaço para o "62"
    val alturaLabelDia = 20.dp // Espaço para o "14"

    // 3. LAZYROW para scroll horizontal
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaMaximaBarra + alturaLabelDia + alturaLabelValor), // Altura total do componente
        // Alinha todas as barras pela base
        verticalAlignment = Alignment.Bottom,
        // Espaço entre as barras
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(dados) { dado ->
            val isSelected = dado.dia == diaSelecionado
            val corBarra = if (isSelected) GraficoDiaSelecionado else GraficoDiaNaoSelecionado
            // Calcula a altura da barra como uma fração do valor máximo
            val alturaFracaoBarra = (dado.valor / maxValor).coerceIn(0f, 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom, // Alinha os itens da coluna em baixo
                modifier = Modifier
                    .fillMaxHeight() // Ocupa a altura total do LazyRow
                    .clickable { onDiaClick(dado.dia) } // É clicável
            ) {
                // 1. Label do Valor (ex: "62")
                Box(
                    modifier = Modifier.height(alturaLabelValor),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (isSelected) { // Só mostra o valor se estiver selecionado
                        Text(
                            text = dado.valor.toString(),
                            fontSize = 12.sp,
                            color = corBarra,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                // 2. A Barra
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        // Usa a altura calculada
                        .height(alturaMaximaBarra * alturaFracaoBarra)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(corBarra)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 3. Label do Dia (ex: "14")
                Box(modifier = Modifier.height(alturaLabelDia)) {
                    Text(
                        text = dado.dia,
                        fontSize = 12.sp,
                        color = CinzaIcones
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsBPMView(stats: StatsBPM) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Media mensal BPM", color = CinzaIcones, style = MaterialTheme.typography.bodyMedium)
            Text("${stats.media}", color = GraficoDiaSelecionado, fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Maior BPM", color = CinzaIcones, style = MaterialTheme.typography.bodyMedium)
            Text("${stats.maior}", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Menor BPM", color = CinzaIcones, style = MaterialTheme.typography.bodyMedium)
            Text("${stats.menor}", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AddPacienteDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var uuid by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.GroupAdd, contentDescription = "Adicionar Paciente") },
        title = { Text("Adicionar Paciente") },
        text = {
            Column {
                Text("Por favor, insira o código (UUID) único do paciente que você deseja adicionar.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uuid,
                    onValueChange = { uuid = it },
                    label = { Text("Código do Paciente (UUID)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(uuid) },
                enabled = uuid.isNotBlank()
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

//===== Cartão do Paciente =====
@OptIn(ExperimentalFoundationApi::class) // Necessário para o detectTapGestures
@Composable
private fun PacienteCard(
    paciente: PacienteSimulado,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit // <-- NOVO PARÂMETRO
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // MUDANÇA: Adicionado pointerInput para 'segurar'
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() }, // Clique normal
                    onLongPress = { onDelete() } // Segurar para excluir
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = paciente.iconeRes),
                contentDescription = "Foto do Paciente",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = paciente.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${paciente.bpmAtual} bpm",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selecionado",
                    tint = AzulGradienteFim
                )
            }
        }
    }
}

//===== Diálogo de Confirmação =====
@Composable
private fun ConfirmarSelecaoPacienteDialog(
    pacienteNome: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.PersonSearch, contentDescription = "Selecionar") },
        title = { Text("Selecionar Paciente") },
        text = { Text("Deseja ver os detalhes de '$pacienteNome'?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulGradienteFim
                )
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// ===== Confirmação de EXCLUSÃO =====
@Composable
private fun ConfirmDeleteDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Warning, contentDescription = "Aviso") },
        title = { Text(text = "Confirmar Exclusão") },
        text = { Text(text = "Você tem certeza que deseja excluir permanentemente o paciente '$itemName'?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error // Cor vermelha
                )
            ) {
                Text("Excluir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}