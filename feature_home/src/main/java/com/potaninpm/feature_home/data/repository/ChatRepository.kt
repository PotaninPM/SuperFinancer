package com.potaninpm.feature_home.data.repository

import com.potaninpm.feature_home.domain.model.ChatAnswer

interface ChatRepository {
    suspend fun getChatAnswer(question: String): ChatAnswer
}