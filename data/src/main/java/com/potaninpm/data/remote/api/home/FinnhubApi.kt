package com.potaninpm.data.remote.api.home

import com.potaninpm.data.ApiConstants
import com.potaninpm.data.remote.dto.home.finnhubDto.FinnhubProfileDto
import com.potaninpm.data.remote.dto.home.finnhubDto.FinnhubQuoteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApi {

    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String = ApiConstants.FINNHUB_API_KEY
    ): FinnhubQuoteDto

    @GET("stock/profile2")
    suspend fun getProfile(
        @Query("symbol") symbol: String,
        @Query("token") token: String = ApiConstants.FINNHUB_API_KEY
    ): FinnhubProfileDto
}