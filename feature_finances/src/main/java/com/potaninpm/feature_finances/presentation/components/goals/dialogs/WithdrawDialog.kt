package com.potaninpm.feature_finances.presentation.components.goals.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun WithdrawDialog(
    goal: GoalEntity,
    onDismiss: () -> Unit,
    onConfirm: (Long, String?) -> Unit,
    onDeleteGoal: () -> Unit
) {
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Снять деньги") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it

                        val amount = amountText.toLongOrNull() ?: 0L
                        when {
                            amount > goal.currentAmount -> {
                                errorText = "Сумма превышает доступный баланс (${goal.currentAmount} ${goal.currency})"
                            }
                            else -> {
                                errorText = null
                            }
                        }
                    },
                    label = { Text("Сумма для снятия") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorText != null,
                )
                if (errorText != null) {
                    Text(
                        text = errorText!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(start = 12.dp)
                    )
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Комментарий (опционально)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val amount = amountText.toLongOrNull() ?: 0L
                when {
                    amount > goal.currentAmount -> {
                        errorText = "Сумма превышает доступный баланс (${goal.currentAmount} ${goal.currency})"
                    }
                    amount == goal.currentAmount -> {
                        onDeleteGoal()
                    }
                    else -> {
                        onConfirm(amount, comment.takeIf { it.isNotBlank() })
                    }
                }
            }) {
                Text("Снять")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


