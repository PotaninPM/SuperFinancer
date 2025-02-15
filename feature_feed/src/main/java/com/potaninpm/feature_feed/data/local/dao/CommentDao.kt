package com.potaninpm.feature_feed.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.potaninpm.feature_feed.data.local.entities.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Insert
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY date ASC")
    fun getComments(postId: Long): Flow<List<CommentEntity>>
}