package com.potaninpm.feature_home.domain.repository

import com.potaninpm.feature_home.domain.model.NewsArticle

interface NewsRepository {
    suspend fun getLatestNews(): List<NewsArticle>

    suspend fun searchNews(query: String): List<NewsArticle>
}