package com.example.salus.data.repository

import com.example.salus.data.model.N8nChatRequest
import com.example.salus.data.network.N8nApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatbotRepository @Inject constructor(
    private val n8nApiService: N8nApiService
) {

    /**
     * Envia a mensagem do usuário para o serviço n8n e retorna a resposta da IA.
     */
    suspend fun postMessage(message: String): Result<String?> { // Retorno agora é anulável
        return try {
            val request = N8nChatRequest(chatInput = message)
            val response = n8nApiService.postChatMessage(request)

            if (response.isSuccessful) {
                // Sucesso! Retorna o texto da resposta, que pode ser nulo.
                // Removido o arriscado "!!" para mais segurança.
                Result.success(response.body()?.text)
            } else {
                // A API retornou um erro (ex: 404, 500)
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API"
                Result.failure(Exception("API Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            // Falha na rede (ex: sem internet)
            Result.failure(e)
        }
    }
}
