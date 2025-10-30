package com.example.salus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe Application customizada.
 * A anotação @HiltAndroidApp habilita a geração de código do Hilt e cria
 * um contêiner de dependências a nível de aplicativo. Essencial para Hilt.
 */
@HiltAndroidApp
class SalusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Ponto para inicializações globais, se necessário no futuro
        // (Ex: bibliotecas de logging, analytics, etc.)
    }
}