package com.example.salus.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.telephony.SmsManager
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

object AlertManager {

    private const val NUMERO_PADRAO_EMERGENCIA = "+5511993410581"

    @SuppressLint("MissingPermission")
    fun sendEmergencySms(context: Context) {
        Log.d("AlertManager", "Iniciando envio de SMS de emergência...")

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location: Location? ->
                val mapsLink = if (location != null) {
                    "https://maps.google.com/maps?q=${location.latitude},${location.longitude}"
                } else {
                    "Localização não encontrada."
                }

                val mensagem = "ALERTA DE EMERGÊNCIA SALUS: Possível AVC detetado. Última localização conhecida: $mapsLink"
                sendSms(context, mensagem)

            }.addOnFailureListener { exception ->
                Log.e("AlertManager", "Falha ao obter localização", exception)
                val mensagem = "ALERTA DE EMERGÊNCIA SALUS: Possível AVC detetado. Não foi possível obter a localização."
                sendSms(context, mensagem)
            }
        } catch (e: Exception) {
            Log.e("AlertManager", "Erro ao disparar alerta de SMS", e)
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendSms(context: Context, message: String) {
        try {
            val smsManager: SmsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }

            smsManager.sendTextMessage(NUMERO_PADRAO_EMERGENCIA, null, message, null, null)
            Log.d("AlertManager", "SMS de alerta enviado para $NUMERO_PADRAO_EMERGENCIA")

        } catch (e: Exception) {
            Log.e("AlertManager", "Falha ao enviar SMS", e)
            e.printStackTrace()
        }
    }
}
