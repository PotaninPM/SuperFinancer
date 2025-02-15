package com.potaninpm.feature_feed.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_feed.data.local.entities.CommentEntity
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.data.repository.PostRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostsViewModel(
    private val repository: PostRepository
): ViewModel() {
    private val currentUser: String = "user1"

    val allPostsFlow: StateFlow<List<PostEntity>> = repository.getAllPostsFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val favoritePostsFlow: StateFlow<List<PostEntity>> = repository.getFavoritePostsFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val myPostsFlow: StateFlow<List<PostEntity>> = repository.getMyPostsFlow(currentUser).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPost(text: String, imageData: List<ByteArray>, tags: List<String>) {
        viewModelScope.launch {
            repository.addPost(
                PostEntity(
                    text = text,
                    imageData = imageData,
                    tags = tags,
                    author = currentUser)
            )
        }
    }

    fun favoritePost(post: PostEntity) {
        viewModelScope.launch {
            repository.updatePost(post.copy(isFavorite = !post.isFavorite))
        }
    }

    fun addComment(postId: Long, commentText: String) {
        viewModelScope.launch {
            repository.addComment(
                CommentEntity(
                    postId = postId,
                    author = currentUser,
                    text = commentText
                )
            )
        }
    }
}