package com.potaninpm.feature_home.data.repository

import com.potaninpm.feature_home.data.remote.api.FinnhubApi
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.domain.repository.TickerRepository

class TickerRepositoryImpl(
    private val finnhubApi: FinnhubApi
) : TickerRepository {
    override suspend fun getTickerInfo(symbol: String): Ticker {
        try {
            val quote = finnhubApi.getQuote(symbol)
            val profile = finnhubApi.getProfile(symbol)

            return Ticker(
                symbol = symbol,
                companyName = profile.name,
                currentPrice = quote.currentPrice,
                currency = profile.currency,
                change = quote.change,
                changePercent = quote.changePercent,
                logoUrl = profile.logo
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return Ticker(
                symbol = symbol,
                companyName = "",
                currentPrice = 0.0f,
                change = 0.0f,
                changePercent = 0.0f,
                logoUrl = "",
                currency = ""
            )
        }
    }

    override suspend fun getTickersInfo(symbols: List<String>): List<Ticker> {
        return symbols.map { getTickerInfo(it) }
    }
}