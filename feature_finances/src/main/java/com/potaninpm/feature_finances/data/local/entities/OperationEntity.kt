package com.potaninpm.feature_finances.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class OperationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val type: String,
    val amount: Double,
    val comment: String? = null,
    val date: Long = System.currentTimeMillis()
)
