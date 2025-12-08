package com.example.salus.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salus.domain.SalusTextToSpeech
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

// Eventos que a UI (Activity/Screen) vai escutar para navegar
sealed class VoiceAction {
    data class Navigate(val route: String) : VoiceAction()
    object TriggerAlert : VoiceAction()
    data class Error(val message: String) : VoiceAction()
}

@HiltViewModel
class VoiceCommandViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Estado: Estamos a ouvir agora?
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    // Evento de ação (Navegar, Alertar, etc)
    private val _voiceAction = MutableStateFlow<VoiceAction?>(null)
    val voiceAction: StateFlow<VoiceAction?> = _voiceAction.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private val tts = SalusTextToSpeech(context) // Nosso motor de fala

    // --- FUNÇÃO PRINCIPAL: Iniciar Escuta ---
    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _voiceAction.value = VoiceAction.Error("Reconhecimento de voz indisponível")
            return
        }

        // Limpa anterior se existir
        speechRecognizer?.destroy()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _isListening.value = true
                }

                override fun onResults(results: Bundle?) {
                    _isListening.value = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.getOrNull(0) ?: ""

                    if (text.isNotBlank()) {
                        processLocalCommand(text)
                    } else {
                        speakFeedback("Não entendi.")
                    }
                }

                override fun onError(error: Int) {
                    _isListening.value = false
                    Log.e("VoiceVM", "Erro de voz: $error")
                    // Opcional: Feedback de erro por voz
                    // speakFeedback("Erro ao ouvir.")
                }

                // Callbacks não usados
                override fun onBeginningOfSpeech() {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { _isListening.value = false }
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onRmsChanged(rmsdB: Float) {}
            })
        }

        // Configura o Intent do Google
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        speechRecognizer?.startListening(intent)
    }

    // --- CÉREBRO LOCAL (Lógica de Palavras-Chave) ---
    private fun processLocalCommand(text: String) {
        val comando = text.lowercase(Locale.getDefault())
        Log.d("VoiceVM", "Comando recebido: $comando")

        when {
            // 1. EMERGÊNCIA
            comando.contains("socorro") ||
                    comando.contains("ajuda") ||
                    comando.contains("alerta") ||
                    comando.contains("emergência") -> {
                speakFeedback("Ativando alerta de emergência.")
                _voiceAction.value = VoiceAction.TriggerAlert
            }

            // 2. NAVEGAÇÃO - Remédios
            comando.contains("remédio") ||
                    comando.contains("medicamento") -> {
                speakFeedback("Abrindo remédios.")
                _voiceAction.value = VoiceAction.Navigate("remedios_paciente") // Use a string da sua rota
            }

            // 3. NAVEGAÇÃO - Mapa
            comando.contains("mapa") ||
                    comando.contains("local") ||
                    comando.contains("onde estou") -> {
                speakFeedback("Abrindo mapa.")
                _voiceAction.value = VoiceAction.Navigate("mapa_paciente")
            }

            // 4. NAVEGAÇÃO - Home
            comando.contains("início") ||
                    comando.contains("home") ||
                    comando.contains("voltar") -> {
                speakFeedback("Voltando ao início.")
                _voiceAction.value = VoiceAction.Navigate("home_paciente")
            }

            // 5. NAVEGAÇÃO - Chatbot (Atalho de voz para o chat)
            comando.contains("chat") ||
                    comando.contains("dúvida") ||
                    comando.contains("pergunta") -> {
                speakFeedback("Abrindo assistente virtual.")
                _voiceAction.value = VoiceAction.Navigate("chatbot")
            }

            // 6. Comando Desconhecido
            else -> {
                speakFeedback("Desculpe, não entendi o comando $text")
                // Não fazemos nada ou mostramos um Toast na UI
            }
        }
    }

    private fun speakFeedback(text: String) {
        tts.speak(text)
    }

    fun clearAction() {
        _voiceAction.value = null
    }

    override fun onCleared() {
        speechRecognizer?.destroy()
        tts.shutdown()
        super.onCleared()
    }
}