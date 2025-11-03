package com.example.salus.ui.screens.cuidador // Ajuste o pacote se necessário

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures // Para o LongPress (segurar)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState // Para o Dialog
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll // Para o Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput // Para o LongPress
import androidx.compose.ui.platform.LocalUriHandler // Para abrir o Google Maps
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog // Para o Pop-up
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar
import com.example.salus.ui.components.SalusVoiceFAB
import com.example.salus.ui.theme.*

// --- Dados Simulados (Copie do RemediosScreenPaciente se precisar) ---
data class RemedioSimulado(
    val nome: String,
    val icone: ImageVector,
    val corIcone: Color,
    var quantidade: Int,
    val apelido: String? = null,
    val dose: String, // ex: "2 comprimidos"
    val frequencia: String // ex: "De 8 em 8 horas"
)
// Usamos 'remember' com 'mutableStateListOf' para a lista ser dinâmica
val remediosSimuladosIniciais = mutableStateListOf(
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
    val endereco: String // Endereço para o Google Maps
)
val consultasSimuladasIniciais = mutableStateListOf(
    ConsultaSimulada(
        data = "29 Setembro, 2024",
        horario = "08:00 AM - 10:30 AM",
        medico = "Dr. Richar Kandowian",
        especialidade = "Cardiologista",
        icone = Icons.Default.Person,
        endereco = "Rua Tito, 54, Vila Romana, São Paulo, SP"
    )
)
data class RemedioFormData(
    val nome: String,
    val apelido: String?, // Opcional
    val dose: String,
    val frequencia: String,
    val quantidade: Int,
    val corIcone: Color
)
data class ConsultaFormData(
    val dataHora: String,
    val endereco: String,
    val medico: String,
    val especialidade: String
)
// ------------------------------------

@Composable
fun RemediosScreenCuidador(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para controlar a visibilidade do pop-up de adicionar
    var showAddDialog by remember { mutableStateOf(false) }

    // Estado para simular a lista de remédios (para podermos adicionar/remover)
    // Usamos 'remember' com 'mutableStateListOf' para a lista ser dinâmica
    val remediosList = remember { remediosSimuladosIniciais }
    val consultasList = remember { consultasSimuladasIniciais }
    // TODO: Usar um ViewModel para gerir esta lista e o paciente selecionado

    // Guardam o item que o utilizador quer apagar. Se for 'null', o pop-up não aparece
    var remedioNomeParaExcluir by remember { mutableStateOf<String?>(null) }
    var consultaParaExcluir by remember { mutableStateOf<ConsultaSimulada?>(null) }

    var remedioEmDetalhe by remember { mutableStateOf<RemedioSimulado?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp), // Padding para a barra
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Botão Adicionar ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { showAddDialog = true }, // Abre o pop-up
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BotaoAdicionar) // Cor verde
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = Color.White)
                }
            }

            // --- Seção Remédios ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                remediosList.forEach { remedio ->
                    key(remedio) {
                        RemedioCardCuidador(
                            remedio = remedio,
                            onStockChange = { change ->
                                val novaQtd = remedio.quantidade + change
                                if (novaQtd >= 0) {
                                    val index = remediosList.indexOf(remedio)
                                    if (index != -1) {
                                        remediosList[index] = remedio.copy(quantidade = novaQtd)
                                    }
                                }
                            },
                            onDelete = {
                                remedioNomeParaExcluir = remedio.nome
                            },
                            onClick = {
                                remedioEmDetalhe = remedio
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // --- Seção Consultas ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Consulta",
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(consultasList) { consulta ->
                key(consulta.medico + consulta.data) {
                    ConsultaCardCuidador(
                        consulta = consulta,
                        onDelete = { consultaParaExcluir = consulta }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // --- Seção Calendário Mini ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CalendarioMiniView(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navController.navigate(AppScreens.CalendarioCuidador.route) // Navega p/ calendário
                        }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- Barra de Navegação Manual ---
        SalusBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentRoute = currentRoute,
            onHomeClick = { navController.navigate(AppScreens.HomeCuidador.route) }, // Vai para Home do Cuidador
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


        if (showAddDialog) {
            AddRemedioConsultaDialog(
                onDismiss = { showAddDialog = false },
                onSaveRemedio = { dadosRemedio ->
                    remediosList.add(
                        RemedioSimulado(
                            nome = dadosRemedio.nome,
                            icone = Icons.Default.Medication,
                            corIcone = dadosRemedio.corIcone,
                            quantidade = dadosRemedio.quantidade,
                            apelido = dadosRemedio.apelido,
                            dose = dadosRemedio.dose,
                            frequencia = dadosRemedio.frequencia
                        )
                    )
                    showAddDialog = false
                },
                onSaveConsulta = { dadosConsulta ->
                    consultasList.add(
                        ConsultaSimulada(
                            data = dadosConsulta.dataHora,
                            horario = "",
                            icone = Icons.Default.Person,
                            medico = dadosConsulta.medico,
                            especialidade = dadosConsulta.especialidade,
                            endereco = dadosConsulta.endereco
                        )
                    )
                    showAddDialog = false
                }
            )
        }

        if (remedioNomeParaExcluir != null) { // <-- MUDANÇA: Verifica o nome
            ConfirmDeleteDialog(
                itemName = remedioNomeParaExcluir!!,
                onConfirm = {
                    remediosList.removeIf { it.nome == remedioNomeParaExcluir }
                    remedioNomeParaExcluir = null // Fecha o diálogo
                },
                onDismiss = {
                    remedioNomeParaExcluir = null // Fecha o diálogo
                }
            )
        }

        if (consultaParaExcluir != null) {
            ConfirmDeleteDialog(
                itemName = "Consulta com ${consultaParaExcluir!!.medico}",
                onConfirm = {
                    consultasList.remove(consultaParaExcluir)
                    consultaParaExcluir = null // Fecha o diálogo
                },
                onDismiss = {
                    consultaParaExcluir = null // Fecha o diálogo
                }
            )
        }
        if (remedioEmDetalhe != null) {
            RemedioDetailsDialog(
                remedio = remedioEmDetalhe!!,
                onDismiss = { remedioEmDetalhe = null }
            )
        }
    }
}

// --- Componentes Internos ---

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RemedioCardCuidador(
    remedio: RemedioSimulado,
    onStockChange: (Int) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // Deteta o "segurar" (LongPress) para excluir
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {onClick() },
                    onLongPress = {
                        onDelete() // Chama a função de exclusão
                    }
                )
            },
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
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            // Botões de Controlo de Stock
            IconButton(onClick = { onStockChange(-1) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Remover 1")
            }
            Box(
                modifier = Modifier
                    .width(48.dp) // Largura ajustada
                    .height(32.dp) // Altura ajustada
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brush.horizontalGradient(listOf(AzulGradienteInicio, AzulGradienteFim))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = remedio.quantidade.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = { onStockChange(1) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar 1")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConsultaCardCuidador(
    consulta: ConsultaSimulada,
    onDelete: () -> Unit
) {
    val uriHandler = LocalUriHandler.current // Para abrir o Google Maps

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Abre o Google Maps com o endereço
                        uriHandler.openUri("https://maps.google.com/maps?q=${consulta.endereco.replace(" ", "+")}")
                    },
                    onLongPress = {
                        onDelete() // Permite excluir
                    }
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        // Parte de cima (Gradiente)
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
        // Parte de baixo (Info Médico)
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
            // Ícones decorativos
            Row {
                Icon(Icons.Default.Link, contentDescription = "Link", tint = CinzaIcones, modifier = Modifier.padding(horizontal = 4.dp))
                Icon(Icons.Default.ChatBubble, contentDescription = "Chat", tint = CinzaIcones, modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}

// ... (Cole aqui o Composable CalendarioMiniView do RemediosScreenPaciente) ...
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


/**
 * O Pop-up para Adicionar Remédio ou Consulta
 * AGORA COM ESTADO E LÓGICA DE 'SAVE'
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRemedioConsultaDialog(
    onDismiss: () -> Unit,
    // ===== MUDANÇA AQUI: Lambdas específicas para salvar =====
    onSaveRemedio: (dados: RemedioFormData) -> Unit,
    onSaveConsulta: (dados: ConsultaFormData) -> Unit
) {
    // --- Estados para os Toggles ---
    var isRemedio by remember { mutableStateOf(true) }
    var isConsulta by remember { mutableStateOf(false) }

    // --- Estados para os Campos de Remédio ---
    var remedioNome by remember { mutableStateOf("") }
    var remedioDataHora by remember { mutableStateOf("") }
    var remedioQuantidade by remember { mutableStateOf("") }
    val coresPredefinidas = listOf(Color(0xFFFFC107), Color(0xFFE91E63), Color(0xFF16C565), Color(0xFF2196F3))
    var corSelecionada by remember { mutableStateOf(coresPredefinidas[0]) }
    var remedioApelido by remember { mutableStateOf("") }
    var remedioDose by remember { mutableStateOf("") }
    var remedioFrequencia by remember { mutableStateOf("") }

    // --- Estados para os Campos de Consulta ---
    var consultaDataHora by remember { mutableStateOf("") }
    var consultaEndereco by remember { mutableStateOf("") }
    var consultaMedico by remember { mutableStateOf("") }
    var consultaEspecialidade by remember { mutableStateOf("") }

    // --- LÓGICA DE VALIDAÇÃO ---
    val isRemedioFormValid = remedioNome.isNotBlank() && remedioDose.isNotBlank() && remedioFrequencia.isNotBlank() && remedioQuantidade.isNotBlank()
    val isConsultaFormValid = consultaDataHora.isNotBlank() && consultaEndereco.isNotBlank() && consultaMedico.isNotBlank() && consultaEspecialidade.isNotBlank()
    val isSaveEnabled = (isRemedio && isRemedioFormValid) || (isConsulta && isConsultaFormValid)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- Cabeçalho do Pop-up ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        modifier = Modifier.clickable { onDismiss() }
                    )
                    Text(
                        text = "Adicionar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    // ===== LÓGICA DE SALVAR ATUALIZADA (COM VALIDAÇÃO) =====
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Salvar",
                        modifier = Modifier.clickable(enabled = isSaveEnabled) {
                            if (isRemedio && isRemedioFormValid) {
                                onSaveRemedio(
                                    RemedioFormData(
                                        nome = remedioNome,
                                        apelido = remedioApelido.ifBlank { null }, // Salva null se vazio
                                        dose = remedioDose,
                                        frequencia = remedioFrequencia,
                                        quantidade = remedioQuantidade.toIntOrNull() ?: 0,
                                        corIcone = corSelecionada
                                    )
                                )
                            } else if (isConsulta && isConsultaFormValid) {
                                onSaveConsulta(
                                    ConsultaFormData(
                                        dataHora = consultaDataHora,
                                        endereco = consultaEndereco,
                                        medico = consultaMedico,
                                        especialidade = consultaEspecialidade
                                    )
                                )
                            }
                        },
                        tint = if (isSaveEnabled) AzulGradienteFim else CinzaIcones
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Toggle Remédio ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nome do remédio", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isRemedio,
                        onCheckedChange = {
                            isRemedio = it
                            if (it) isConsulta = false
                        }
                    )
                }

                // --- Campos de Remédio (Funcionais) ---
                if (isRemedio) {
                    OutlinedTextField(
                        value = remedioNome,
                        onValueChange = { remedioNome = it },
                        label = { Text("Nome do Remédio*") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = remedioNome.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = remedioApelido,
                        onValueChange = { remedioApelido = it },
                        label = { Text("Apelido (ex: O da manhã)") }, // Opcional
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = remedioDose,
                        onValueChange = { remedioDose = it },
                        label = { Text("Dose (ex: 2 comprimidos)*") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = remedioDose.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = remedioFrequencia,
                        onValueChange = { remedioFrequencia = it },
                        label = { Text("Frequência (ex: De 8 em 8 horas)*") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = remedioFrequencia.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = remedioQuantidade,
                        onValueChange = { remedioQuantidade = it },
                        label = { Text("Quantidade Total (ex: 30)*") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = remedioQuantidade.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Cor do Ícone:", style = MaterialTheme.typography.bodyLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        coresPredefinidas.forEach { cor ->
                            ColorSelector(
                                cor = cor,
                                isSelected = cor == corSelecionada,
                                onClick = { corSelecionada = cor }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // --- Toggle Consulta ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Consulta", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isConsulta,
                        onCheckedChange = {
                            isConsulta = it
                            if (it) isRemedio = false
                        }
                    )
                }

                // --- Campos de Consulta (Funcionais) ---
                if (isConsulta) {
                    OutlinedTextField(
                        value = consultaDataHora,
                        onValueChange = { consultaDataHora = it },
                        label = { Text("Data / Horário") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = consultaEndereco,
                        onValueChange = { consultaEndereco = it },
                        label = { Text("Rua, Bairro, -UF") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = consultaMedico,
                        onValueChange = { consultaMedico = it },
                        label = { Text("Dr. Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = consultaEspecialidade,
                        onValueChange = { consultaEspecialidade = it },
                        label = { Text("Especialidade") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSelector(
    cor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(cor)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
    )
}
@Composable
private fun ConfirmDeleteDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Fecha se clicar fora
        // Ícone de Aviso
        icon = { Icon(Icons.Default.Warning, contentDescription = "Aviso") },
        // Título
        title = { Text(text = "Confirmar Exclusão") },
        // Texto
        text = { Text(text = "Você tem certeza que deseja excluir permanentemente o item '$itemName'?") },
        // Botão de Confirmação
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error // Cor vermelha para perigo
                )
            ) {
                Text("Excluir")
            }
        },
        // Botão de Cancelar
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )

}
@Composable
private fun RemedioDetailsDialog(
    remedio: RemedioSimulado,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        // Ícone e Cor
        icon = { Icon(remedio.icone, contentDescription = null, tint = remedio.corIcone, modifier = Modifier.size(48.dp)) },
        // Título (Nome do Remédio)
        title = {
            Text(
                text = remedio.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        // Corpo (Detalhes)
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Apelido (só aparece se existir)
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
        // Botão de Fechar
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

// Componente auxiliar para o RemedioDetailsDialog
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
