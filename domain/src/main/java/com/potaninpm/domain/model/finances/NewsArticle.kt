package com.potaninpm.domain.model.finances

data class NewsArticle(
    val id: String,
    val title: String,
    val abstract: String,
    val webUrl: String,
    val imageUrl: String?,
    val source: String,
    val publishedAt: String
)
