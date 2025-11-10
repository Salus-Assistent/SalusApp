package com.example.salus.services // Ajuste o pacote se necessário

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.SystemClock
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.example.salus.MainActivity
import com.example.salus.navigation.AppScreens
import com.example.salus.domain.AlertManager

/**
 * Este serviço corre em segundo plano (Opção B)
 * para detetar o atalho do botão de volume.
 */
class SalusAccessibilityService : AccessibilityService() {

    private var pressCount = 0
    private var firstPressTime: Long = 0



    override fun onKeyEvent(event: KeyEvent?): Boolean {

        if (event?.action != KeyEvent.ACTION_DOWN) {
            return super.onKeyEvent(event)
        }

        // Verifica se foi o botão de Aumentar Volume
        if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            val currentTime = SystemClock.uptimeMillis()

            if (pressCount == 0) {
                // Este é o primeiro clique
                firstPressTime = currentTime
                pressCount = 1
            } else {
                // Este é o 2º ou 3º clique
                if (currentTime - firstPressTime < PRESS_TIMEOUT) {
                    // O clique está dentro do tempo limite
                    pressCount++
                } else {
                    // Passou muito tempo, reinicia a contagem
                    firstPressTime = currentTime
                    pressCount = 1
                }
            }

            // Se chegámos a 3 cliques
            if (pressCount == 3) {
                // SUCESSO! Dispara o alerta.
                sendEmergencySms()

                // Reinicia a contagem
                pressCount = 0
                firstPressTime = 0
            }

            // Retornamos 'true' para consumir o evento (o volume não vai aumentar)
            // Se quiser que o volume aumente, retorne 'false'
            return true
        }

        // Se for outra tecla, deixa o sistema lidar com ela
        return super.onKeyEvent(event)
    }

    private fun sendEmergencySms() {
        AlertManager.sendEmergencySms(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Não precisamos de detetar eventos de UI, apenas teclas
    }

    override fun onInterrupt() {
        // O serviço foi interrompido
    }

    companion object {
        // O tempo máximo (em milissegundos) entre os 3 cliques
        private const val PRESS_TIMEOUT = 2000 // (2 segundos)
    }
}