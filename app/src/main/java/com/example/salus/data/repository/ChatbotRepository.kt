package com.example.salus.data.repository

import com.example.salus.data.model.N8nChatRequest
import com.example.salus.data.network.N8nApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatbotRepository @Inject constructor(
    private val n8nApiService: N8nApiService
) {

    suspend fun postMessage(message: String): Result<String?> {
        return try {
            val request = N8nChatRequest(chatInput = message)
            val response = n8nApiService.postChatMessage(request)

            if (response.isSuccessful) {
                Result.success(response.body()?.text)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API"
                Result.failure(Exception("API Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
