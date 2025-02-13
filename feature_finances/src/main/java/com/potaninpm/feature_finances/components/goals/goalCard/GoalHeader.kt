package com.potaninpm.feature_finances.components.goals.goalCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GoalHeader(
    title: String,
    currentAmount: Long,
    targetAmount: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Накоплено $currentAmount из $targetAmount ₽",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        GoalProgress(
            currentAmount = currentAmount,
            targetAmount = targetAmount
        )
    }
}