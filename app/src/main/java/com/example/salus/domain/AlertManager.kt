package com.example.salus.domain // Ajuste o pacote se necessário

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.salus.MainActivity
import com.example.salus.navigation.AppScreens
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * Gestor Singleton para centralizar a lógica de Alerta.
 * Pode ser chamado de qualquer Activity, Service ou Composable.
 */
object AlertManager {

    // ===== CONFIGURE AQUI O NÚMERO DE EMERGÊNCIA =====
    // ATENÇÃO: Use o código do país (ex: +351 para Portugal, +55 para Brasil)
    private const val NUMERO_PADRAO_EMERGENCIA = "+5511993410581" // <-- MUDE ISTO
    // ===============================================

    /**
     * O gatilho de alerta principal.
     * Tenta obter a localização e enviar o SMS.
     * Independentemente do sucesso, inicia a navegação de alerta.
     */
    @SuppressLint("MissingPermission") // Nós verificamos as permissões no Onboarding
    fun triggerAlert(context: Context) {
        Log.d("AlertManager", "ALERTA DISPARADO!")

        // --- Ação 2 (Simulação): Navegar para o AlertaScreenCuidador ---
        // (Isto é chamado primeiro para dar feedback imediato ao utilizador)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("START_DESTINATION", AppScreens.AlertaRecebidoCuidador.route)
        }
        context.startActivity(intent)


        // --- Ação 1 (SMS Real): Tentar obter a localização e enviar ---
        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            // Tenta obter a localização atual
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location: Location? ->

                val mapsLink = if (location != null) {
                    "https://maps.google.com/maps?q=$${location.latitude},${location.longitude}"
                } else {
                    "Localização não encontrada."
                }

                val mensagem = "ALERTA DE EMERGÊNCIA SALUS: Possível AVC detetado. Última localização conhecida: $mapsLink"

                sendSms(context, mensagem)

            }.addOnFailureListener {
                // Falha ao obter localização, envia SMS sem o link
                Log.e("AlertManager", "Falha ao obter localização", it)
                val mensagem = "ALERTA DE EMERGÊNCIA SALUS: Possível AVC detetado. Não foi possível obter a localização."
                sendSms(context, mensagem)
            }

        } catch (e: Exception) {
            Log.e("AlertManager", "Erro ao disparar alerta", e)
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission") // Verificamos no Onboarding
    private fun sendSms(context: Context, message: String) {
        try {
            // Obter o SmsManager
            // (Para APIs mais recentes, podemos precisar de obter por getSystemService)
            val smsManager: SmsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }

            // Envia o SMS
            smsManager.sendTextMessage(NUMERO_PADRAO_EMERGENCIA, null, message, null, null)
            Log.d("AlertManager", "SMS de alerta enviado para $NUMERO_PADRAO_EMERGENCIA")

        } catch (e: Exception) {
            Log.e("AlertManager", "Falha ao enviar SMS", e)
            e.printStackTrace()
        }
    }
}