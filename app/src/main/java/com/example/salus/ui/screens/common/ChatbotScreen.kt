package com.example.salus.ui.screens.paciente

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.salus.data.model.ChatMessage
import com.example.salus.navigation.AppScreens // <--- IMPORTANTE
import com.example.salus.ui.components.MarkdownText
import com.example.salus.ui.components.SalusBottomBar // <--- IMPORTANTE
import com.example.salus.ui.theme.AzulGradienteFim
import com.example.salus.viewmodel.ChatbotViewModel
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavHostController,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // --- LÓGICA DE VOZ ---
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)
            if (!spokenText.isNullOrBlank()) {
                viewModel.sendMessage(spokenText)
                textInput = ""
            }
        }
    }

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("SIA") }) },

        // --- ADICIONADO: A BARRA DE NAVEGAÇÃO ---
        bottomBar = {
            SalusBottomBar(
                currentRoute = AppScreens.Chatbot.route, // Marca o botão Chatbot como ativo (se houver)
                onHomeClick = { navController.navigate(AppScreens.HomePaciente.route) },
                onCalendarClick = { navController.navigate(AppScreens.CalendarioPaciente.route) },
                onNotificationsClick = { navController.navigate(AppScreens.NotificacoesPaciente.route) },
                onSettingsClick = { navController.navigate(AppScreens.PerfilPaciente.route) }
            )
        },
        // ----------------------------------------

        modifier = Modifier.imePadding()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // LISTA DE MENSAGENS
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
            ) {
                items(chatMessages) { message ->
                    ChatBubble(message)
                }
                if (isLoading) {
                    item {
                        Text("A SIA está a pensar...", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 8.dp))
                    }
                }
            }

            // --- ÁREA DE INPUT (Fica logo acima da BottomBar) ---
            Surface(
                tonalElevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                    // navigationBarsPadding removido daqui pois o BottomBar já cuida disso
                    ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // CAMPO DE TEXTO
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Digite sua dúvida...") },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 56.dp),
                        shape = RoundedCornerShape(28.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // BOTÃO DINÂMICO (Mic / Send)
                    val isMicMode = textInput.isBlank()

                    FloatingActionButton(
                        onClick = {
                            if (isMicMode) {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Pode falar...")
                                }
                                voiceLauncher.launch(intent)
                            } else {
                                if (!isLoading) {
                                    viewModel.sendMessage(textInput)
                                    textInput = ""
                                }
                            }
                        },
                        containerColor = AzulGradienteFim,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(75.dp)
                    ) {
                        AnimatedContent(targetState = isMicMode, label = "icon_anim") { showMic ->
                            if (showMic) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Falar",
                                    modifier = Modifier.size(36.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Enviar",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// (Seu ChatBubble com MarkdownText continua aqui igual)
@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.isFromUser
    val align = if (isUser) Alignment.End else Alignment.Start
    val containerColor = if (isUser) MaterialTheme.colorScheme.surfaceVariant else AzulGradienteFim.copy(alpha = 0.15f)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = align) {
        Surface(
            color = containerColor,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isUser) 16.dp else 2.dp, bottomEnd = if (isUser) 2.dp else 16.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            MarkdownText(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}