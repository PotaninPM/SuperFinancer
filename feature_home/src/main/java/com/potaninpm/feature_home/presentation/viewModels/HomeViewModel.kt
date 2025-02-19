package com.potaninpm.feature_home.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_home.domain.model.NewsArticle
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
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

    fun loadTickersData() {
        viewModelScope.launch {
            val tickerSymbols = tickerRepository.getTickers().map { it.ticker }

            val tickersDeferred = async { tickerRepository.getTickersInfo(tickerSymbols) }
            val newsDeferred = async { newsRepository.getLatestNews() }

            _tickers.value = tickersDeferred.await()
            _news.value = newsDeferred.await()
        }
    }

    fun refreshTickersData() {
        viewModelScope.launch {
            val tickerSymbols = tickerRepository.getTickers().map { it.ticker }

            val tickersDeferred = async { tickerRepository.getTickersInfo(tickerSymbols) }

            _tickers.value = tickersDeferred.await()
            _newTickerDataLoaded.value = true
        }
    }

    fun refreshNewsData() {
        viewModelScope.launch {
            val newsDeferred = async { newsRepository.getLatestNews() }
            _news.value = newsDeferred.await()
        }
    }
}

