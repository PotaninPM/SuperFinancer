package com.potaninpm.data.remote.api.home

import com.potaninpm.data.ApiConstants
import com.potaninpm.data.remote.dto.home.nytDto.NYTimesArticleResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTimesApi {
    @GET("articlesearch.json")
    suspend fun getArticles(
        @Query("q") query: String = "",
        @Query("api-key") apiKey: String = ApiConstants.NYTIMES_API_KEY
    ): NYTimesArticleResponseDto
}