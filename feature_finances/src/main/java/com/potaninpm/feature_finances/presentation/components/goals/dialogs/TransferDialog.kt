package com.potaninpm.feature_finances.presentation.components.goals.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun TransferDialog(
    fromGoal: GoalEntity,
    availableTargetGoals: List<GoalEntity>,
    onDismiss: () -> Unit,
    onConfirm: (Long, Long, Long, String?) -> Unit
) {
    var selectedTarget by remember { mutableStateOf(if (availableTargetGoals.isNotEmpty()) availableTargetGoals.first() else null) }
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val amountValue = amountText.toLongOrNull()

    val amountError = when {
        amountText.isEmpty() -> "Сумма не может быть пустой"
        amountValue == null -> "Введите корректное число"
        amountValue > fromGoal.currentAmount -> "Недостаточно средств (макс. ${fromGoal.currentAmount})"
        else -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Перевести деньги") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(
                    value = fromGoal.title,
                    hint = "Откуда перевод",
                    type = "text",
                    enabled = false,
                    isError = false,
                    error = null,
                    onValueChange = {},
                    borderColor = null,
                    onClick = {}
                )

                Box {
                    CustomTextField(
                        value = selectedTarget?.title.orEmpty(),
                        hint = "Куда перевести",
                        type = "text",
                        enabled = false,
                        isError = false,
                        error = null,
                        onValueChange = {},
                        borderColor = null,
                        onClick = { expanded = true }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        availableTargetGoals.forEach { targetGoal ->
                            DropdownMenuItem(
                                text = { Text(targetGoal.title) },
                                onClick = {
                                    selectedTarget = targetGoal
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                CustomTextField(
                    value = amountText,
                    hint = "Сумма перевода",
                    type = "number",
                    enabled = true,
                    isError = amountError != null,
                    error = amountError,
                    onValueChange = { amountText = it },
                    borderColor = null,
                    onClick = {}
                )

                CustomTextField(
                    value = comment,
                    hint = "Комментарий (опционально)",
                    type = "text",
                    enabled = true,
                    isError = false,
                    error = null,
                    onValueChange = { comment = it },
                    borderColor = null,
                    onClick = {}
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    if (selectedTarget != null) {
                        onConfirm(fromGoal.id, selectedTarget!!.id, amount, comment.takeIf { it.isNotBlank() })
                    }
                },
                enabled = amountError == null && amountText.isNotEmpty() && selectedTarget != null
            ) {
                Text("Перевести")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
