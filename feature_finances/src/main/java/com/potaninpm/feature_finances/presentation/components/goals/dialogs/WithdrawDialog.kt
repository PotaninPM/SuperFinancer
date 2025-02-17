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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun WithdrawDialog(
    goal: GoalEntity,
    onDismiss: () -> Unit,
    onConfirm: (Long, String?) -> Unit,
    onDeleteGoal: () -> Unit
) {
    val context = LocalContext.current
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.withdraw_money)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it

                        val amount = amountText.toLongOrNull() ?: 0L
                        errorText = when {
                            amount > goal.currentAmount -> {
                                context.getString(
                                    R.string.sum_is_bigger_than_balance,
                                    goal.currentAmount.toString(),
                                    goal.currency
                                )
                            }

                            else -> {
                                null
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.sum_to_withdraw)) },
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
                    label = { Text(stringResource(R.string.comments_optional)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val amount = amountText.toLongOrNull() ?: 0L
                when {
                    amount > goal.currentAmount -> {
                        context.getString(
                            R.string.sum_is_bigger_than_balance,
                            goal.currentAmount.toString(),
                            goal.currency
                        )
                        errorText = context.getString(
                            R.string.sum_is_bigger_than_balance,
                            goal.currentAmount.toString(),
                            goal.currency
                        )
                    }
                    amount == goal.currentAmount -> {
                        onDeleteGoal()
                    }
                    else -> {
                        onConfirm(amount, comment.takeIf { it.isNotBlank() })
                    }
                }
            }) {
                Text(stringResource(R.string.withdraw))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


