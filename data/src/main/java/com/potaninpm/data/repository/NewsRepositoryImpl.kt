package com.potaninpm.data.repository

import com.potaninpm.data.remote.api.NYTimesApi
import com.potaninpm.data.remote.mappers.toDomainNews
import com.potaninpm.domain.model.NewsArticle
import com.potaninpm.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val nyTimesApi: NYTimesApi
) : NewsRepository {
    override suspend fun getLatestNews(): List<NewsArticle> {
        try {
            val response = nyTimesApi.getArticles(query = "finance")
            val articles = response.response.docs.map { dto ->
                dto.toDomainNews()
            }
            return articles
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}