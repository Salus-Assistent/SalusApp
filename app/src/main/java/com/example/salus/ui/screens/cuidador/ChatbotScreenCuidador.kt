package com.example.salus.ui.screens.cuidador // Ajuste o pacote se necessário

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.components.SalusBottomBar
import com.example.salus.ui.components.SalusVoiceFAB
import com.example.salus.ui.theme.*

// --- Dados Simulados para o Chat ---
data class ChatMessage(val text: String, val isFromUser: Boolean)

// Mensagem de boas-vindas da SIA
val welcomeMessage = ChatMessage(
    text = "Olá, Mateus! Eu sou a SIA, sua assistente virtual. Como posso ajudar hoje? Você pode me perguntar sobre AVC ou sobre o app.",
    isFromUser = false
)
// ------------------------------------

@Composable
fun ChatbotScreenCuidador(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para a mensagem que o usuário está a digitar
    var userMessage by remember { mutableStateOf("") }

    // Lista de mensagens do chat (começa com a boas-vindas)
    val chatMessages = remember { mutableStateListOf(welcomeMessage) }

    // --- LAYOUT MANUAL COM BOX ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // FundoClaro
    ) {

        // 1. Conteúdo do Ecrã (Column)
        // Usamos Column (não rolável) para fixar a barra de digitação em baixo
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Padding para não ficar atrás da barra de navegação (90dp)
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Avatar da SIA ---
            Image(
                painter = painterResource(id = R.drawable.sia), // Avatar da SIA
                contentDescription = "Avatar SalusAI",
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SIA (Assistente Virtual)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // --- Lista de Mensagens ---
            // Usamos LazyColumn com weight(1f) para ocupar todo o espaço restante
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                reverseLayout = true // Mensagens novas aparecem em baixo
            ) {
                // Invertemos a lista para o reverseLayout funcionar corretamente
                items(chatMessages.reversed()) { message ->
                    MessageBubble(message = message)
                }
            }

            // --- Barra de Digitação ---
            OutlinedTextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                placeholder = { Text("Digite sua pergunta aqui...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        // --- Lógica de Chat Simulada ---
                        if (userMessage.isNotBlank()) {
                            // Adiciona a mensagem do usuário
                            chatMessages.add(ChatMessage(userMessage, true))
                            val userMessageCopy = userMessage
                            userMessage = ""

                            // Resposta simulada da IA
                            // (Aqui entraria a lógica de IA real)
                            val response = getSimulatedResponse(userMessageCopy)
                            chatMessages.add(ChatMessage(response, false))
                        }
                    }) {
                        Icon(Icons.Default.Send, contentDescription = "Enviar", tint = AzulGradienteFim)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulGradienteFim,
                    unfocusedBorderColor = CinzaIcones
                )
            )
        }

        // 2. A Barra de Navegação (MANUAL)
        SalusBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentRoute = currentRoute,
            onHomeClick = { navController.navigate(AppScreens.HomeCuidador.route) },
            onCalendarClick = { navController.navigate(AppScreens.CalendarioCuidador.route) },
            onNotificationsClick = { navController.navigate(AppScreens.NotificacoesCuidador.route) },
            onSettingsClick = { navController.navigate(AppScreens.PerfilCuidador.route) }
        )

        // 3. O Botão FAB (MANUAL)
        SalusVoiceFAB(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp),
            onClick = { /* TODO: Lógica de voz */ }
        )
    }
}

// --- Componente Privado para a Bolha de Chat ---
@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        // Alinha à direita se for do usuário, à esquerda se for da IA
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 0.dp,
                bottomEnd = if (message.isFromUser) 0.dp else 16.dp
            ),
            // Cor azul se for do usuário, cinza claro se for da IA
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) AzulGradienteInicio else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.widthIn(max = 300.dp) // Limita a largura da bolha
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isFromUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// --- Lógica Simulada de Resposta da IA ---
private fun getSimulatedResponse(userMessage: String): String {
    val msg = userMessage.lowercase()
    return when {
        "avc" in msg || "sintomas" in msg -> "Os principais sintomas do AVC (FAST) são: Rosto caído (Face), fraqueza nos Braços (Arms) e dificuldade na Fala (Speech). Se notar isso, o Tempo (Time) é crucial. Ligue 192."
        "remédio" in msg || "consulta" in msg -> "Você pode ver seus remédios e consultas na tela 'Remédios' ou na tela 'Calendário'."
        "olá" in msg || "oi" in msg -> "Olá! Como posso te ajudar?"
        else -> "Desculpe, eu só posso responder perguntas sobre AVC e sobre o aplicativo Salus."
    }
}
