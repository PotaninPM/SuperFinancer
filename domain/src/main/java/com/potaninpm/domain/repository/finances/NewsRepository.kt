package com.potaninpm.domain.repository.finances

import com.potaninpm.domain.model.finances.NewsArticle

interface NewsRepository {
    suspend fun getLatestNews(): List<NewsArticle>
}