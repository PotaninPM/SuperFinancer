package com.potaninpm.feature_feed.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.presentation.components.PostCard
import com.potaninpm.feature_feed.presentation.viewModels.PostsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: PostsViewModel = koinViewModel()
) {
    val allPosts by viewModel.allPostsFlow.collectAsState()
    val favoritePosts by viewModel.favoritePostsFlow.collectAsState()
    val myPosts by viewModel.myPostsFlow.collectAsState()

    var showAddPostDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    if (showAddPostDialog) {

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TabRow(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
                        selectedTabIndex = currentTab
                    ) {
                        listOf("Все посты", "Избранные", "Мои").forEachIndexed { index, title ->
                            Tab(
                                selected = currentTab == index,
                                onClick = {
                                    currentTab = index
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                },
                                text = { Text(title) }
                            )
                        }
                    }
                },
                actions = {

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddPostDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add post")
            }
        },

    ) { innerPadding ->
        /*HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> PostList(
                    posts = allPosts,
                    onPostClick = {

                    },
                    onLongClickFavorite = {
                        viewModel.favoritePost(it)
                    },
                    onAddComment = { post, comment ->
                        viewModel.addComment(post.id, comment)
                    }
                )
                1 -> PostList(
                    posts = favoritePosts,
                    onPostClick = {

                    },
                    onLongClickFavorite = {
                        viewModel.favoritePost(it)
                    },
                    onAddComment = { post, comment ->
                        viewModel.addComment(post.id, comment)
                    }
                )
                2 -> PostList(
                    posts = myPosts,
                    onPostClick = {

                    },
                    onLongClickFavorite = { viewModel.favoritePost(it) },
                    onAddComment = { post, comment ->
                        viewModel.addComment(post.id, comment)
                    }
                )
            }
        }*/
    }
}

@Composable
fun PostList(
    posts: List<PostEntity>,
    onPostClick: (PostEntity) -> Unit,
    onLongClickFavorite: (PostEntity) -> Unit,
    onAddComment: (PostEntity, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onPostClick = onPostClick,
                onFavorite = onLongClickFavorite,
                onAddComment = onAddComment
            )
        }
    }
}
