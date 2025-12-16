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

    private val tts = SalusTextToSpeech(context)

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val initialMessage = "Olá! Eu sou a SIA. Como posso ajudar com a sua saúde hoje?"
        _chatMessages.value = listOf(ChatMessage(initialMessage, false))
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            _chatMessages.update { it + ChatMessage(userMessage, true) }
            _isLoading.value = true

            try {
                val result = chatbotRepository.postMessage(userMessage)
                result.onSuccess { responseText ->
                    if (!responseText.isNullOrBlank()) {
                        _chatMessages.update { it + ChatMessage(responseText, false) }
                        tts.speak(responseText)
                    } else {
                        val errorMsg = "Não recebi uma resposta válida. Tente novamente."
                        _chatMessages.update { it + ChatMessage(errorMsg, false) }
                        tts.speak(errorMsg)
                    }
                }.onFailure { exception ->
                    exception.printStackTrace()
                    val errorMsg = "Desculpe, estou com problemas de conexão."
                    _chatMessages.update { it + ChatMessage(errorMsg, false) }
                    tts.speak(errorMsg)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

 
    override fun onCleared() {
        tts.shutdown()
        super.onCleared()
    }
}
