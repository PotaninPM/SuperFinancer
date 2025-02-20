package com.potaninpm.feature_home.presentation.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.potaninpm.core.ui.components.shimmerCards.ShimmerNewsCard
import com.potaninpm.core.ui.components.shimmerCards.ShimmerSearchTicker
import com.potaninpm.core.ui.components.shimmerCards.ShimmerTickerCard
import com.potaninpm.core.ui.screens.FullScreenImageDialog
import com.potaninpm.feature_home.R
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
import java.net.URLEncoder

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val tickersState by viewModel.tickers.collectAsState()
    val newsState by viewModel.news.collectAsState()
    val newTickerDataLoaded by viewModel.newTickerDataLoaded.collectAsState()

    val prefs = context.getSharedPreferences("ticker_settings", Context.MODE_PRIVATE)
    var autoUpdateEnabled by rememberSaveable { mutableStateOf(prefs.getBoolean("auto_update_enabled", false)) }

    var showSettingsDialog by remember { mutableStateOf(false) }

    var updateInterval by remember {
        mutableLongStateOf(prefs.getLong("update_interval", 10L))
    }

    var remainingTime by remember { mutableIntStateOf(updateInterval.toInt()) }

    var aiBottomSheet by remember { mutableStateOf(false) }

    var chosenTickerCompany by remember { mutableStateOf("") }

    if (aiBottomSheet) {
        ChatAiBottomSheet(
            companyName = chosenTickerCompany,
            onDismiss = {
                aiBottomSheet = false
            }
        )
    }
    LaunchedEffect(Unit) {
        viewModel.loadTickersData()
    }

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

            }
        },
        rootNavController = rootNavController,
        onTickerClick = { ticker ->
            chosenTickerCompany = ticker
            aiBottomSheet = true
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
    rootNavController: NavHostController,
    newsState: List<NewsArticle>,
    tickersState: List<Ticker>,
    autoUpdateEnabled: Boolean,
    remainingTime: Int,
    onSettingsClick: () -> Unit,
    onNewsRefreshClick: () -> Unit,
    onTickerRefreshClick: () -> Unit,
    onFakeSearchClick: () -> Unit,
    onTickerClick: (String) -> Unit
) {
    val listState = rememberScrollState()

    var selectedUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var imageClicked by remember { mutableStateOf("") }

    if (imageClicked.isNotEmpty()) {
         FullScreenImageDialog(
             imageUrl = imageClicked,
             onDismiss = { imageClicked = "" }
         )
    }

    if (selectedUrl == null) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.main),
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
                    },
                    onTickerClick = { ticker ->
                        onTickerClick(ticker)
                    }
                )

                NewsList(
                    newsState,
                    onClick = {
                        selectedUrl = it
                    },
                    onNewsRefreshClick = {
                        onNewsRefreshClick()
                    },
                    onImageClicked = { imageUrl ->
                        imageClicked = imageUrl
                    }
                )
            }
        }
    } else {
        val encodedUrl = URLEncoder.encode(selectedUrl, "UTF-8")
        rootNavController.navigate("article_web_view/$encodedUrl")

        selectedUrl = null
    }
}

@Composable
fun NewsList(
    newsState: List<NewsArticle>,
    onClick: (String) -> Unit,
    onNewsRefreshClick: () -> Unit,
    onImageClicked: (String) -> Unit
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
            text = stringResource(R.string.news),
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
                    },
                    onImageClicked = { imageUrl ->
                        onImageClicked(imageUrl)
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
    onRefreshClick: () -> Unit,
    onTickerClick: (String) -> Unit
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
            text = stringResource(R.string.tickers),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(bottom = 8.dp)
        )

        if (autoUpdateEnabled) {
            val minutes = remainingTime / 60
            val seconds = remainingTime % 60
            Text(
                text = String.format(stringResource(R.string.will_update_02d_02d), minutes, seconds),
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
            ) {
                items(tickers) { ticker ->
                    TickerCard(
                        ticker = ticker,
                        onTickerClick = {
                            ticker.companyName?.let { onTickerClick(it) }
                        }
                    )
                }
            }
        }
    }
}