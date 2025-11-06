package com.example.salus.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salus.BuildConfig
import com.example.salus.data.model.ChatMessage
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor() : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val systemInstruction = content(role = "system") {
        text("""
            Você é a SIA, uma assistente virtual do aplicativo Salus.
            Sua única responsabilidade é responder a perguntas sobre dois tópicos:
            1. Sintomas, prevenção e primeiros socorros relacionados a AVC (Acidente Vascular Cerebral).
            2. Funcionalidades e como usar o aplicativo Salus (ex: como adicionar remédios, como funciona o alerta, como gerir cuidadores).

            Seja sempre acolhedora, empática e clara. Use linguagem simples, como se estivesse a falar com um idoso ou alguém com pouca tecnologia.

            Se o utilizador perguntar sobre CUALQUER outro tópico (como o tempo, filmes, política, receitas, matemática, etc.),
            você DEVE recusar educadamente e reforçar o seu propósito.
            Exemplo de recusa: "Peço desculpa, mas eu sou a SIA e só consigo ajudar com dúvidas sobre AVC e sobre o nosso aplicativo Salus. Posso ajudar com algum desses tópicos?"
        """.trimIndent())
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro", // Revertido para o modelo mais estável para o teste final
        apiKey = BuildConfig.GEMINI_API_KEY, 
        systemInstruction = systemInstruction
    )

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("Olá, SIA.") },
            content(role = "model") {
                text("Olá! Eu sou a SIA, a assistente virtual do Salus. Estou aqui para ajudar com qualquer dúvida que você tenha sobre AVC ou sobre como usar o aplicativo. O que gostaria de saber?")
            }
        )
    )

    init {
        _chatMessages.value = chat.history
            .filter { it.role == "model" }
            .map {
                ChatMessage(
                    text = it.parts.first().asTextOrNull() ?: "",
                    isFromUser = false
                )
            }
    }

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return

        viewModelScope.launch {
            _chatMessages.update { it + ChatMessage(userMessage, true) }
            _isLoading.value = true

            try {
                val response = chat.sendMessage(userMessage)

                response.text?.let { responseText ->
                    _chatMessages.update { currentList ->
                        currentList + ChatMessage(responseText, false)
                    }
                }
            } catch (e: Exception) {
                // Log aprimorado para depuração
                Log.e("ChatbotViewModel", "API call failed with exception: ${e.localizedMessage}", e)
                _chatMessages.update { it + ChatMessage("Desculpe, ocorreu um erro técnico. Não consegui me conectar ao serviço de IA.", false) }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
