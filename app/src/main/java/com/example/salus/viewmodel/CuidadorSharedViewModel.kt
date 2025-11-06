package com.example.salus.viewmodel // Ajuste o pacote se necessário

import androidx.lifecycle.ViewModel
import com.example.salus.R
import com.example.salus.data.model.PacienteSimulado // <-- Importa a data class
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Este ViewModel é partilhado entre TODOS os ecrãs do fluxo de Cuidador.
 * A sua responsabilidade é guardar "Qual paciente está selecionado agora?".
 */
@HiltViewModel
class CuidadorSharedViewModel @Inject constructor() : ViewModel() {

    // Lista inicial de pacientes simulados
    private val pacientesIniciais = listOf(
        PacienteSimulado("id_paciente_1", "Carlos Almeida", R.drawable.icon_masc, 72),
        PacienteSimulado("id_paciente_2", "Maria Joaquina", R.drawable.icon_fem, 78)
    )

    private val _pacientesDisponiveis = MutableStateFlow(pacientesIniciais)
    val pacientesDisponiveis: StateFlow<List<PacienteSimulado>> = _pacientesDisponiveis.asStateFlow()


    // --- O ESTADO PARTILHADO ---
    private val _pacienteSelecionado = MutableStateFlow<PacienteSimulado?>(null)
    val pacienteSelecionado: StateFlow<PacienteSimulado?> = _pacienteSelecionado.asStateFlow()

    init {
        // Tenta selecionar o primeiro paciente da lista por defeito
        if (_pacienteSelecionado.value == null && _pacientesDisponiveis.value.isNotEmpty()) {
            _pacienteSelecionado.value = _pacientesDisponiveis.value[0]
        }
    }

    /**
     * Função chamada pelo 'RegistrosScreenCuidador' para atualizar o paciente ativo.
     */
    fun selecionarPaciente(paciente: PacienteSimulado) {
        _pacienteSelecionado.value = paciente
    }

    /**
     * Função chamada pelo 'RegistrosScreenCuidador' para adicionar um paciente.
     * (Simulação do MVP)
     */
    fun adicionarPaciente(uuid: String) {
        val novoPaciente = PacienteSimulado(
            id = "id_${uuid.take(4)}",
            nome = "Novo Paciente ($uuid)",
            iconeRes = R.drawable.icon_masc, // (Usar um avatar genérico)
            bpmAtual = 70
        )
        // Adiciona à lista (isto irá atualizar automaticamente a UI que observa 'pacientesDisponiveis')
        _pacientesDisponiveis.update { listaAtual -> listaAtual + novoPaciente }
    }

    /**
     * Função chamada pelo 'RegistrosScreenCuidador' para remover um paciente.
     * (Simulação do MVP)
     */
    fun removerPaciente(paciente: PacienteSimulado) {
        _pacientesDisponiveis.update { listaAtual -> listaAtual.filter { it.id != paciente.id } }

        // Se o paciente removido era o selecionado, limpa a seleção (ou seleciona o próximo)
        if (_pacienteSelecionado.value?.id == paciente.id) {
            _pacienteSelecionado.value = _pacientesDisponiveis.value.firstOrNull()
        }
    }
}