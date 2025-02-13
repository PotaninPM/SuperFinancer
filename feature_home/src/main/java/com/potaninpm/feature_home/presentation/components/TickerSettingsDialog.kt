package com.potaninpm.feature_home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TickerSettingsDialog(
    currentInterval: Long,
    autoUpdateEnabled: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Long, Boolean) -> Unit
) {
    var intervalText by rememberSaveable { mutableStateOf(currentInterval.toString()) }
    var isError by rememberSaveable { mutableStateOf("") }
    var isAutoUpdateEnabled by rememberSaveable { mutableStateOf(autoUpdateEnabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Настройки обновления тикеров") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Switch(
                        checked = isAutoUpdateEnabled,
                        onCheckedChange = { isAutoUpdateEnabled = it }
                    )
                    Text("Включить автообновление", modifier = Modifier.padding(start = 8.dp))
                }

                if (isAutoUpdateEnabled) {
                    OutlinedTextField(
                        value = intervalText,
                        onValueChange = {
                            intervalText = it
                            isError = when {
                                it.isEmpty() -> "Поле не может быть пустым"
                                (it.toIntOrNull() ?: 0) < 8 -> "Интервал не может быть меньше 8 секунд"
                                else -> ""
                            }
                        },
                        label = { Text("Интервал обновления (сек.)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isError.isNotEmpty(),
                        supportingText = {
                            if (isError.isNotEmpty()) {
                                Text(text = isError)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = (isAutoUpdateEnabled && isError.isEmpty() && intervalText.isNotEmpty()) || autoUpdateEnabled,
                onClick = {
                    val newInterval = intervalText.toLongOrNull() ?: currentInterval
                    onConfirm(newInterval, isAutoUpdateEnabled)
                }
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

