package com.example.salus.data.model // Ou o seu pacote

import com.example.salus.R // Importe o R

// (Pode adicionar os outros imports de dados simulados aqui também)

data class PacienteSimulado(
    val id: String,
    val nome: String,
    val iconeRes: Int,
    val bpmAtual: Int
)

// Podemos mover a lista inicial para o ViewModel