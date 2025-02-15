package com.potaninpm.feature_home.presentation.screens

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.potaninpm.core.components.shimmerCards.ShimmerNewsCard
import com.potaninpm.core.components.shimmerCards.ShimmerTickerCard
import com.potaninpm.feature_home.domain.model.NewsArticle
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.presentation.components.NewsCard
import com.potaninpm.feature_home.presentation.components.TickerCard
import com.potaninpm.feature_home.presentation.components.TickerSettingsDialog
import com.potaninpm.feature_home.presentation.components.searchBar.FakeSearchBar
import com.potaninpm.feature_home.presentation.navigation.RootNavDestinations
import com.potaninpm.feature_home.presentation.viewModels.HomeViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val tickersState by viewModel.tickers.collectAsState()
    val newsState by viewModel.news.collectAsState()
    val newTickerDataLoaded by viewModel.newTickerDataLoaded.collectAsState()

    val prefs = context.getSharedPreferences("ticker_settings", Context.MODE_PRIVATE)
    var autoUpdateEnabled by rememberSaveable { mutableStateOf(prefs.getBoolean("auto_update_enabled", true)) }

    var showSettingsDialog by remember { mutableStateOf(false) }

    var updateInterval by remember {
        mutableLongStateOf(prefs.getLong("update_interval", 10L))
    }

    var remainingTime by remember { mutableIntStateOf(updateInterval.toInt()) }

    LaunchedEffect(autoUpdateEnabled) {
        if (autoUpdateEnabled) {
            while (true) {
                remainingTime = 0
                viewModel.refreshTickersData()


                while (!newTickerDataLoaded) {
                    delay(300)
                }

                remainingTime = updateInterval.toInt()
                delay(updateInterval * 1000L)
            }
        }
    }

    LaunchedEffect(remainingTime) {
        if (autoUpdateEnabled) {
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime--
            }
        }
    }

    HomeScreenContent(
        newsState = newsState,
        tickersState = tickersState,
        autoUpdateEnabled = autoUpdateEnabled,
        onSettingsClick = {
            showSettingsDialog = true
        },
        remainingTime = remainingTime,
        onTickerRefreshClick = {
            viewModel.refreshTickersData()
        },
        onNewsRefreshClick = {
            viewModel.refreshNewsData()
        },
        onFakeSearchClick = {
            rootNavController.navigate(RootNavDestinations.Search.route) {
                //popUpTo(RootNavDestinations.Search) { inclusive = true }
            }
        }
    )

    if (showSettingsDialog) {
        TickerSettingsDialog(
            currentInterval = updateInterval,
            autoUpdateEnabled = autoUpdateEnabled,
            onDismiss = { showSettingsDialog = false },
            onConfirm = { newInterval, isAutoUpdateEnabled ->
                updateInterval = newInterval
                autoUpdateEnabled = isAutoUpdateEnabled

                prefs.edit()
                    .putLong("update_interval", newInterval)
                    .putBoolean("auto_update_enabled", isAutoUpdateEnabled)
                    .apply()
                showSettingsDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    newsState: List<NewsArticle>,
    tickersState: List<Ticker>,
    autoUpdateEnabled: Boolean,
    remainingTime: Int,
    onSettingsClick: () -> Unit,
    onNewsRefreshClick: () -> Unit,
    onTickerRefreshClick: () -> Unit,
    onFakeSearchClick: () -> Unit
) {
    val listState = rememberScrollState()

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
                        },
                        actions = {
                            IconButton(onClick = { onSettingsClick() }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp)
                    .verticalScroll(listState)
            ) {
                FakeSearchBar {
                    onFakeSearchClick()
                }

                TickersList(
                    tickersState,
                    autoUpdateEnabled = autoUpdateEnabled,
                    remainingTime = remainingTime,
                    onRefreshClick = {
                        onTickerRefreshClick()
                    }
                )


                NewsList(
                    newsState,
                    onClick = {
                        selectedUrl = it
                    },
                    onNewsRefreshClick = {
                        onNewsRefreshClick()
                    }
                )
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
    onClick: (String) -> Unit,
    onNewsRefreshClick: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        delay(1000)
        isRefreshing = false
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Новости",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(bottom = 8.dp)
        )

        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .alpha(if (isRefreshing) 0.5f else 1f)
                .clickable(enabled = !isRefreshing) {
                    isRefreshing = true
                    onNewsRefreshClick()
                }
        )
    }

    if (newsState.isNotEmpty()) {
        if (isRefreshing) {
            for (i in 0..1) {
                ShimmerNewsCard()
            }
        } else {
            newsState.forEach { article ->
                NewsCard(
                    article = article,
                    onClick = {
                        onClick(article.webUrl)
                    }
                )
            }
        }
    } else {
        for (i in 0..1) {
            ShimmerNewsCard()
        }
    }
}

@Composable
fun TickersList(
    tickers: List<Ticker>,
    autoUpdateEnabled: Boolean,
    remainingTime: Int,
    onRefreshClick: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        delay(3000)
        isRefreshing = false
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Тикеры",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(bottom = 8.dp)
        )

        if (autoUpdateEnabled) {
            val minutes = remainingTime / 60
            val seconds = remainingTime % 60
            Text(
                text = String.format("Обновится через %02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .alpha(if (isRefreshing) 0.5f else 1f)
                    .clickable(enabled = !isRefreshing) {
                        isRefreshing = true
                        onRefreshClick()
                    }
            )
        }
    }

    if (tickers.isEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(3) {
                ShimmerTickerCard()
            }
        }
    } else {
        if (isRefreshing) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(3) {
                    ShimmerTickerCard()
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(tickers) { ticker ->
                    TickerCard(ticker = ticker)
                }
            }
        }
    }
}