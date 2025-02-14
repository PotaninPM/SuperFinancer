package com.potaninpm.feature_finances.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.data.local.mappers.toDomain
import com.potaninpm.feature_finances.data.repository.GoalsRepository
import com.potaninpm.feature_finances.data.repository.OperationsRepository
import com.potaninpm.feature_finances.domain.model.Operation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinancesViewModel(
    private val goalRepository: GoalsRepository,
    private val operationRepository: OperationsRepository
): ViewModel() {

    val goals: StateFlow<List<GoalEntity>> =
        goalRepository.getGoals().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val operations: StateFlow<List<Operation>> =
        operationRepository.getOperations()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalSavings: StateFlow<Long> = goals.map { list -> list.sumOf { it.currentAmount } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    val overallProgress: StateFlow<Float> = goals.map { list ->
        val totalTarget = list.sumOf { it.targetAmount }
        if (totalTarget > 0) (list.sumOf { it.currentAmount }.toFloat() / totalTarget * 100) else 0f
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

    fun addGoal(title: String, targetAmount: Long, currency: String, dueDate: Long?) {
        viewModelScope.launch {
            goalRepository.addGoal(
                GoalEntity(
                    title = title,
                    targetAmount = targetAmount,
                    currency = currency,
                    dueDate = dueDate
                )
            )
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            if (goal.currentAmount > 0) {
                operationRepository.addOperation(
                    OperationEntity(
                        goalId = goal.id,
                        type = "withdrawal",
                        amount = -goal.currentAmount.toDouble(),
                        comment = "Цель удалена"
                    )
                )
            }
            goalRepository.deleteGoal(goal)
        }
    }

    fun addDeposit(
        goal: GoalEntity,
        amount: Long,
        comment: String?
    ) {
        viewModelScope.launch {
            operationRepository.addOperation(
                OperationEntity(
                    goalId = goal.id,
                    type = "deposit",
                    amount = amount.toDouble(),
                    comment = comment
                )
            )

            val updatedGoal = goal.copy(currentAmount = goal.currentAmount + amount)
            goalRepository.updateGoal(updatedGoal)
        }
    }

    fun addWithdrawal(
        goal: GoalEntity,
        amount: Long,
        comment: String?
    ) {
        viewModelScope.launch {
            operationRepository.addOperation(
                OperationEntity(
                    goalId = goal.id,
                    type = "withdrawal",
                    amount = -amount.toDouble(),
                    comment = comment
                )
            )

            val updatedGoal = goal.copy(currentAmount = goal.currentAmount - amount)
            goalRepository.updateGoal(updatedGoal)
        }
    }
}