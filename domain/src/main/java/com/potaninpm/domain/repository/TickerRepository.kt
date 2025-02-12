package com.potaninpm.domain.repository

import com.potaninpm.domain.model.Ticker

interface TickerRepository {
    suspend fun getTickerInfo(symbol: String): Ticker
    suspend fun getTickersInfo(symbols: List<String>): List<Ticker>
}