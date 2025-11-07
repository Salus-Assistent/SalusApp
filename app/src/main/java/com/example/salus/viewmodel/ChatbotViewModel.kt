package com.example.salus.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salus.data.model.ChatMessage
import com.example.salus.data.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Olá! Eu sou a SIA, a assistente virtual do Salus. Estou aqui para ajudar com qualquer dúvida que você tenha sobre AVC ou sobre como usar o aplicativo. O que gostaria de saber?",
                isFromUser = false
            )
        )
    }

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return

        viewModelScope.launch {
            _chatMessages.update { it + ChatMessage(userMessage, true) }
            _isLoading.value = true

            val result = chatbotRepository.postMessage(userMessage)

            result.onSuccess { responseText ->
                // CORRIGIDO: Verifica se a resposta não é nula ou vazia antes de usar
                if (!responseText.isNullOrBlank()) {
                    _chatMessages.update { currentList ->
                        currentList + ChatMessage(responseText, false)
                    }
                } else {
                    // A resposta do n8n veio vazia, então mostramos um erro controlado
                    Log.e("ChatbotViewModel", "n8n response was successful but the text is null or blank.")
                    _chatMessages.update { it + ChatMessage("Desculpe, recebi uma resposta vazia. Verifique o workflow no n8n.", false) }
                }
            }.onFailure { exception ->
                Log.e("ChatbotViewModel", "n8n webhook call failed", exception)
                _chatMessages.update { it + ChatMessage("Desculpe, ocorreu um erro técnico. Não consegui me conectar.", false) }
            }

            _isLoading.value = false
        }
    }
}
