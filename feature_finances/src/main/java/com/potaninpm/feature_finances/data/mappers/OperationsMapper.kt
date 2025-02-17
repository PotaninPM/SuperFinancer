package com.potaninpm.feature_finances.data.mappers

import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.domain.Operation

fun OperationEntity.toDomain(): Operation {
    return Operation(
        date = this.date,
        title = "Операция #$id",
        subtitle = "Цель: $goalId",
        amount = this.amount,
        type = this.type,
        currency = this.currency,
        comment = this.comment,
    )
}

fun Operation.toEntity(goalId: Long, type: String): OperationEntity {
    return OperationEntity(
        goalId = goalId,
        type = type,
        amount = this.amount,
        currency = this.currency,
        comment = this.comment,
        date = this.date
    )
}