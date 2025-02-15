package com.potaninpm.feature_home.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.potaninpm.feature_home.domain.model.SearchResults
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flowOf

class SearchViewModel(
    private val newsRepository: NewsRepository,
    private val tickerRepository: TickerRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    var searchResults: StateFlow<SearchResults> = _query
        .debounce(500)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { q ->
            if (q.isBlank()) {
                flowOf(SearchResults(emptyList(), emptyList()))
            } else {
                flow {
                    coroutineScope {
                        val newsD = async { newsRepository.searchNews(q) }
                        val tickersD = async { tickerRepository.searchTickers(q) }
                        val news = newsD.await()
                        val tickers = tickersD.await().take(10)
                        emit(SearchResults(news, tickers))
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, SearchResults(emptyList(), emptyList()))
}