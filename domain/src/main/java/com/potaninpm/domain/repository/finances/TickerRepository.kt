package com.potaninpm.domain.repository.finances

import com.potaninpm.domain.model.finances.Ticker

interface TickerRepository {
    suspend fun getTickerInfo(symbol: String): Ticker
    suspend fun getTickersInfo(symbols: List<String>): List<Ticker>
}