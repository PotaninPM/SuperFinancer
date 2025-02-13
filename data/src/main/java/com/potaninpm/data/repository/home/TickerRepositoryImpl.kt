package com.potaninpm.data.repository.home

import com.potaninpm.data.remote.api.home.FinnhubApi
import com.potaninpm.domain.model.finances.Ticker
import com.potaninpm.domain.repository.finances.TickerRepository

class TickerRepositoryImpl(val finnhubApi: FinnhubApi) : TickerRepository {
    override suspend fun getTickerInfo(symbol: String): Ticker {
        try {
            val quote = finnhubApi.getQuote(symbol)
            val profile = finnhubApi.getProfile(symbol)

            return Ticker(
                symbol = symbol,
                companyName = profile.name,
                currentPrice = quote.currentPrice,
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
                logoUrl = ""
            )
        }
    }

    override suspend fun getTickersInfo(symbols: List<String>): List<Ticker> {
        return symbols.map { getTickerInfo(it) }
    }
}