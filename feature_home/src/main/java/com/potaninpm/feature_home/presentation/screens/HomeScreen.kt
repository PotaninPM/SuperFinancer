package com.potaninpm.feature_home.presentation.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.potaninpm.domain.model.NewsArticle
import com.potaninpm.domain.model.Ticker
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.presentation.components.NewsCard
import com.potaninpm.feature_home.presentation.components.TickerCard
import com.potaninpm.feature_home.presentation.viewModels.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val tickersState by viewModel.tickers.collectAsState()
    val newsState by viewModel.news.collectAsState()

    HomeScreenContent(
        newsState = newsState,
        tickersState = tickersState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    newsState: List<NewsArticle>,
    tickersState: List<Ticker>
) {
    val listState = rememberLazyListState()

    var selectedUrl by rememberSaveable { mutableStateOf<String?>(null) }

    if (selectedUrl == null) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Главная",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    )

                    //SearchBar()

                    HorizontalDivider(
                        modifier = Modifier
                            .height(12.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 80.dp)
                ) {
//                item {
//                    HorizontalDivider(modifier = Modifier.height(12.dp))
//                }

                    item {
                        TickersList(tickersState)
                    }

                    item {
                        NewsList(
                            newsState,
                            onClick = {
                                selectedUrl = it
                            }
                        )
                    }
                }
            }
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Статья") },
                    navigationIcon = {
                        IconButton(onClick = { selectedUrl = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            AndroidView(
                modifier = Modifier.padding(innerPadding),
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(selectedUrl!!)
                    }
                }
            )
        }
    }
}

@Composable
fun NewsList(
    newsState: List<NewsArticle>,
    onClick: (String) -> Unit
) {
    Text(
        text = "Новости",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(top = 16.dp)
    )

    newsState.forEach { article ->
        NewsCard(
            article = article,
            onClick = {
                onClick(article.webUrl)
            }
        )
    }
}

@Composable
fun TickersList(tickers: List<Ticker>) {
    Text(
        text = "Тикеры",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(bottom = 8.dp)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tickers) { ticker ->
            TickerCard(ticker = ticker)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchText = remember { mutableStateOf("") }
    var active = remember { mutableStateOf(false) }

    val colors1 = SearchBarDefaults.colors()

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchText.value,
                onQueryChange = { searchText.value = it },
                onSearch = {
                    active.value = false
                },
                expanded = active.value,
                onExpandedChange = { active.value = it },
                enabled = true,
                placeholder = { Text("Поиск...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Иконка поиска"
                    )
                },
                trailingIcon = null,
                interactionSource = null,
            )
        },
        expanded = active.value,
        onExpandedChange = { active.value = it },
        modifier = Modifier,
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors1,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        content = {
            Text("Content")
        }
    )
}

//@Preview
//@Composable
//private fun HomeScreenDarkPreview() {
//    MaterialTheme(
//        colorScheme = darkColorScheme()
//    ) {
//        HomeScreenContent()
//    }
//}
//
//@Preview
//@Composable
//private fun HomeScreenLightPreview() {
//    MaterialTheme(
//        colorScheme = lightColorScheme()
//    ) {
//        HomeScreenContent()
//    }
//}