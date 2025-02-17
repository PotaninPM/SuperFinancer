package com.potaninpm.feature_feed.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.potaninpm.feature_feed.R
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.presentation.components.CommentsBottomSheet
import com.potaninpm.feature_feed.presentation.components.PostCard
import com.potaninpm.feature_feed.presentation.viewModels.PostsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    rootNavController: NavController,
    postsViewModel: PostsViewModel = koinViewModel()
) {
    val allPosts by postsViewModel.allPostsFlow.collectAsState()
    val favoritePosts by postsViewModel.favoritePostsFlow.collectAsState()
    val myPosts by postsViewModel.myPostsFlow.collectAsState()

    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    var selectedUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedPost by remember { mutableStateOf<PostEntity?>(null) }

    if (selectedPost != null) {
        CommentsBottomSheet(
            post = selectedPost!!,
            onDismiss = { selectedPost = null }
        )
    }

    if (selectedUrl == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                        ) {
                            listOf("Все посты", "Любимые", "Мои").forEachIndexed { index, title ->
                                Text(
                                    text = title,
                                    modifier = Modifier
                                        .clickable {
                                            currentTab = index
                                            scope.launch { pagerState.animateScrollToPage(index) }
                                        },
                                    color = if (currentTab == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                    fontWeight = if (currentTab == index) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    },
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                            }

                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                rootNavController.navigate("create_post/${""}")
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_circle_24px),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(bottom = 85.dp)
            ) { page ->
                when (page) {
                    0 -> PostList(
                        posts = allPosts,
                        onPostClick = {

                        },
                        onLongClickFavorite = {
                            postsViewModel.favoritePost(it)
                        },
                        onShowComments = {
                            selectedPost = it
                        },
                        onArticleClick = {
                            selectedUrl = it
                        }
                    )

                    1 -> PostList(
                        posts = favoritePosts,
                        onPostClick = {

                        },
                        onLongClickFavorite = {
                            postsViewModel.favoritePost(it)
                        },
                        onShowComments = {
                            selectedPost = it
                        },
                        onArticleClick = {
                            selectedUrl = it
                        }
                    )

                    2 -> PostList(
                        posts = myPosts,
                        onPostClick = {

                        },
                        onLongClickFavorite = { postsViewModel.favoritePost(it) },
                        onShowComments = {
                            selectedPost = it
                        },
                        onArticleClick = {
                            selectedUrl = it
                        }
                    )
                }
            }
        }
    } else {
        ArticleWebView(
            selectedUrl = selectedUrl!!,
            onCreateClick = {

            },
            rootNavController = rootNavController,
            onBackClick = {
                selectedUrl = null
            }
        )
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Статья") },
//                    navigationIcon = {
//                        IconButton(onClick = { selectedUrl = null }) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Назад"
//                            )
//                        }
//                    }
//                )
//            }
//        ) { innerPadding ->
//            AndroidView(
//                modifier = Modifier.padding(innerPadding),
//                factory = { context ->
//                    WebView(context).apply {
//                        webViewClient = WebViewClient()
//                        settings.javaScriptEnabled = true
//                        loadUrl(selectedUrl!!)
//                    }
//                }
//            )
//        }
    }
}

@Composable
fun PostList(
    posts: List<PostEntity>,
    onPostClick: (PostEntity) -> Unit,
    onArticleClick: (String) -> Unit,
    onShowComments: (PostEntity) -> Unit,
    onLongClickFavorite: (PostEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onPostClick = onPostClick,
                onLongPostClick = onLongClickFavorite,
                onArticleClick = onArticleClick,
                onFavorite = onLongClickFavorite,
                onShowComments = {
                    onShowComments(post)
                }
            )
        }
    }
}
