package com.potaninpm.data.remote.mappers

import com.potaninpm.data.remote.dto.home.nytDto.NYTimesArticleDto
import com.potaninpm.domain.model.finances.NewsArticle
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun NYTimesArticleDto.toDomainNews(): NewsArticle {
    val imageUrl = multimedia?.firstOrNull { it.subtype == "xlarge" }?.url

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy '|' HH:mm")
    val dateTime = OffsetDateTime.parse(pubDate, inputFormatter)

    val formattedDate = dateTime.format(outputFormatter)

    return NewsArticle(
        id = id,
        title = headline.main,
        abstract = abstract ?: "",
        webUrl = webUrl,
        imageUrl = imageUrl?.let { "https://www.nytimes.com/$it" },
        source = source,
        publishedAt = formattedDate
    )
}