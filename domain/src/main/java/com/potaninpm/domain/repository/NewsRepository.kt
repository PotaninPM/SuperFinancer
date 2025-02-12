package com.potaninpm.domain.repository

import com.potaninpm.domain.model.NewsArticle

interface NewsRepository {
    suspend fun getLatestNews(): List<NewsArticle>
}