package com.potaninpm.feature_home.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_home.domain.model.SearchResults
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(
    private val newsRepository: NewsRepository,
    private val tickerRepository: TickerRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _searchResults = MutableStateFlow(SearchResults(emptyList(), emptyList()))
    val searchResults: StateFlow<SearchResults> = _searchResults

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .collect { q ->
                    if (q.isBlank()) {
                        _searchResults.value = SearchResults(emptyList(), emptyList())
                    } else {
                        try {
                            val news = newsRepository.searchNews(q)
                            val tickers = tickerRepository.searchTickers(q).take(10)
                            _searchResults.value = SearchResults(news, tickers)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _searchResults.value = SearchResults(emptyList(), emptyList())
                        }
                    }
                }
        }
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun clearAll() {
        _query.value = ""
        _searchResults.value = SearchResults(emptyList(), emptyList())
    }
}