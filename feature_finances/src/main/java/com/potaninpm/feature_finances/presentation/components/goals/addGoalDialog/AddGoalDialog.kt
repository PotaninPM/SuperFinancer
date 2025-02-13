package com.potaninpm.feature_finances.presentation.components.goals.addGoalDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.components.DatePicker
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onAddGoal: (String, Long, Long?) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var targetSum by rememberSaveable { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    if (showDatePicker) {
        DatePicker(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismissRequest = { showDatePicker = false }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая цель") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название цели") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = targetSum,
                    onValueChange = { sum ->
                        targetSum = sum
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Требуемая сумма") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = selectedDate?.format(dateFormatter) ?: "Выбрать дату",
                    onValueChange = {

                    },
                    readOnly = true,
                    label = { Text("Дата") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val targetAmount = targetSum.toLongOrNull() ?: 0L
                    val dueDateTimestamp = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                    onAddGoal(name, targetAmount, dueDateTimestamp)
                }
            ) {
                Text(stringResource(R.string.save), fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
