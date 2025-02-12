package com.potaninpm.domain.model

data class NewsArticle(
    val id: String,
    val title: String,
    val abstract: String,
    val imageUrl: String?,
    val source: String,
    val publishedAt: String
)
