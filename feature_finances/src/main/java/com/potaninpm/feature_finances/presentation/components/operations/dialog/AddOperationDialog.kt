package com.potaninpm.feature_finances.presentation.components.operations.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun AddOperationDialog(
    goals: List<GoalEntity>,
    onDismiss: () -> Unit,
    onAddOperation: (Long, Long, String?) -> Unit
) {
    var selectedGoal by remember { mutableStateOf(if (goals.isNotEmpty()) goals.first() else null) }
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }

    var amountError by rememberSaveable { mutableStateOf<String?>(null) }
    val maxAmount = selectedGoal?.targetAmount?.minus(selectedGoal?.currentAmount ?: 0L) ?: 0L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_operation)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (goals.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedTextField(
                            value = goals.firstOrNull { it.id == selectedGoal?.id }?.title ?: "",
                            onValueChange = {

                            },
                            label = { Text(stringResource(R.string.target)) },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            goals.forEach { goal ->
                                DropdownMenuItem(
                                    text = { Text(goal.title) },
                                    onClick = {
                                        selectedGoal = goal
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                CustomTextField(
                    value = amountText,
                    hint = "Сумма операции (${goals.firstOrNull { it.id == selectedGoal?.id }?.currency ?: "₽"})",
                    type = "number",
                    isError = amountError != null,
                    error = amountError,
                    onValueChange = {
                        amountText = it
                        amountError = when {
                            it.isEmpty() -> "Сумма не может быть пустой"
                            it.toLongOrNull() == null -> "Введите корректное число"
                            it.toLong() + selectedGoal?.currentAmount!! > maxAmount -> "Сумма пополнения больше цели, максимум ${maxAmount}"
                            else -> null
                        }
                    }
                )

                CustomTextField(
                    value = comment,
                    hint = "Комментарий (опционально)",
                    isError = false,
                    error = null,
                    onValueChange = { comment = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    val goal = selectedGoal ?: return@Button
                    onAddOperation(goal.id, amount, comment.takeIf { it.isNotBlank() })
                },
                enabled = amountError == null && amountText.isNotEmpty()
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
