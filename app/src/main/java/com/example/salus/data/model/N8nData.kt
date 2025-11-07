package com.example.salus.data.model

import com.google.gson.annotations.SerializedName

data class N8nChatRequest(
    val chatInput: String
)

data class N8nChatResponse(
    @SerializedName("output")
    val text: String? 
)
