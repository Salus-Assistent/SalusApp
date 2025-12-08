package com.example.salus.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salus.data.model.ChatMessage
import com.example.salus.data.repository.ChatbotRepository
import com.example.salus.domain.SalusTextToSpeech
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {

    // Inicializa o TTS
    private val tts = SalusTextToSpeech(context)

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Mensagem inicial fixa (Resolve a complexidade do repositório)
        val initialMessage = "Olá! Eu sou a SIA. Como posso ajudar com a sua saúde hoje?"
        _chatMessages.value = listOf(ChatMessage(initialMessage, false))

        // Se quiser que ela fale ao abrir, descomente:
        // tts.speak(initialMessage)
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            // 1. Adiciona mensagem do utilizador à lista (UI)
            _chatMessages.update { it + ChatMessage(text = userMessage, isFromUser = true) }
            _isLoading.value = true

            try {
                // 2. Envia para o Gemini via Repository
                // O objeto 'chat' vem do repositório
                val response = chatbotRepository.chat.sendMessage(userMessage)

                // 3. Pega o texto da resposta (pode ser nulo)
                val responseText: String? = response.text

                // 4. Verifica e atualiza
                if (responseText != null) {
                    _chatMessages.update { it + ChatMessage(text = responseText, isFromUser = false) }
                    tts.speak(responseText) // Fala a resposta
                } else {
                    val errorMsg = "Não entendi. Pode repetir?"
                    _chatMessages.update { it + ChatMessage(text = errorMsg, isFromUser = false) }
                    tts.speak(errorMsg)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val errorMsg = "Sem conexão. Tente mais tarde."
                _chatMessages.update { it + ChatMessage(text = errorMsg, isFromUser = false) }
                tts.speak(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpar o TTS quando sair da tela
    override fun onCleared() {
        tts.shutdown()
        super.onCleared()
    }
}