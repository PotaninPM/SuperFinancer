package com.potaninpm.feature_home.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.domain.model.NewsArticle
import com.potaninpm.domain.model.Ticker
import com.potaninpm.domain.repository.NewsRepository
import com.potaninpm.domain.repository.TickerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val tickerRepository: TickerRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _tickers = MutableStateFlow<List<Ticker>>(emptyList())
    val tickers: StateFlow<List<Ticker>> = _tickers

    private val _news = MutableStateFlow<List<NewsArticle>>(emptyList())
    val news: StateFlow<List<NewsArticle>> = _news

    private val _newTickerDataLoaded = MutableStateFlow(false)
    val newTickerDataLoaded: StateFlow<Boolean> = _newTickerDataLoaded

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val tickerSymbols = listOf("AAPL", "GOOG", "MSFT", "TSLA")
            val tickersDeferred = async { tickerRepository.getTickersInfo(tickerSymbols) }
            val newsDeferred = async { newsRepository.getLatestNews() }

            _tickers.value = tickersDeferred.await()
            _news.value = newsDeferred.await()
            Log.i("INFOG", _tickers.value.toString())
            Log.i("INFOG", _news.value.toString())
        }
    }

    fun refreshTickersData() {
        viewModelScope.launch {
            val tickerSymbols = listOf("AAPL", "GOOG", "MSFT", "TSLA")
            val tickersDeferred = async { tickerRepository.getTickersInfo(tickerSymbols) }

            _tickers.value = tickersDeferred.await()
            Log.i("INFOG2", _tickers.value.toString())
            _newTickerDataLoaded.value = true
        }
    }
}
