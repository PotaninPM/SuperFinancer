package com.potaninpm.feature_home.domain.repository

import com.potaninpm.feature_home.domain.model.Ticker

interface TickerRepository {
    suspend fun getTickerInfo(symbol: String): Ticker
    suspend fun getTickersInfo(symbols: List<String>): List<Ticker>
}