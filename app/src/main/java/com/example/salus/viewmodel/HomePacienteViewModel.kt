package com.example.salus.viewmodel // Ajuste o pacote se necessário

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * ViewModel para a tela principal do Paciente.
 * Responsável por gerir os dados simulados (como o BPM)
 * e a lógica de negócio do ecrã HomePaciente.
 */
@HiltViewModel
class HomePacienteViewModel @Inject constructor() : ViewModel() {

    private val _nomeUsuario = MutableStateFlow("Mario")
    val nomeUsuario: StateFlow<String> = _nomeUsuario.asStateFlow()

    // --- BPM Simulado Dinâmico ---
    private val _bpmSimulado = MutableStateFlow((68..75).random()) // Valor inicial aleatório
    val bpmSimulado: StateFlow<Int> = _bpmSimulado.asStateFlow()

    init {
        // Inicia a simulação automática de BPM
        iniciarSimulacaoBPM()
    }

    private fun iniciarSimulacaoBPM() {
        // viewModelScope garante que esta corrotina é cancelada
        // automaticamente quando o ViewModel é destruído.
        viewModelScope.launch {
            while (true) {
                // Gera um novo valor de BPM aleatório
                val novoBPM = Random.nextInt(68, 76) // (68 a 75)

                // Atualiza o StateFlow (a UI será notificada)
                _bpmSimulado.value = novoBPM

                // Espera um tempo aleatório (ex: entre 3 a 7 segundos)
                val tempoEspera = Random.nextLong(3000, 7001)
                delay(tempoEspera)
            }
        }
    }

    // TODO: Adicionar lógica para o Botão de Alerta (Fase 3 do Roadmap)
    // fun onAlertaClicked() { ... }
}