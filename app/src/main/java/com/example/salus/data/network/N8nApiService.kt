package com.example.salus.data.network

import com.example.salus.data.model.N8nChatRequest
import com.example.salus.data.model.N8nChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface N8nApiService {

    /**
     * Envia uma mensagem para o webhook do chatbot no n8n local (URL de Produção).
     */
    @POST("webhook/ecb0695a-39d6-4af8-be9a-069dd981fd92")
    suspend fun postChatMessage(
        @Body request: N8nChatRequest
    ): Response<N8nChatResponse>
}
