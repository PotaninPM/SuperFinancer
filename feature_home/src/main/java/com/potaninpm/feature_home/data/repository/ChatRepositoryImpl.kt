package com.potaninpm.feature_home.data.repository

import com.potaninpm.feature_home.data.mappers.toDomain
import com.potaninpm.feature_home.data.remote.api.ChatApi
import com.potaninpm.feature_home.data.remote.dto.chat.ChatMessageDto
import com.potaninpm.feature_home.domain.model.ChatAnswer
import com.potaninpm.feature_home.domain.model.ChatRequest
import com.potaninpm.feature_home.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val chatApi: ChatApi
): ChatRepository {
    override suspend fun getChatAnswer(question: String): ChatAnswer {
        val request = ChatRequest(
            model = "meta-llama/llama-3.3-70b-instruct",
            messages = listOf(
                ChatMessageDto("financial advisor", question)
            )
        )

        return chatApi.getChatAnswer(request).toDomain()
    }
}