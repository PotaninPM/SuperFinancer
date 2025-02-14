package com.potaninpm.feature_finances.data.local.mappers

import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.domain.model.Operation

fun OperationEntity.toDomain(): Operation {
    return Operation(
        date = this.date,
        title = "Операция #$id",
        subtitle = "Тип: $type, Цель: $goalId",
        amount = this.amount,
        comment = this.comment
    )
}

fun Operation.toEntity(goalId: Long, type: String): OperationEntity {
    return OperationEntity(
        goalId = goalId,
        type = type,
        amount = this.amount,
        comment = this.comment,
        date = this.date
    )
}