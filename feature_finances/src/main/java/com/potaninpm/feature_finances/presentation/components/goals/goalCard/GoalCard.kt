package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GoalCard(
    title: String,
    currentAmount: Long,
    targetAmount: Long
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GoalHeader(
                title = title,
                currentAmount = currentAmount,
                targetAmount = targetAmount
            )

            Spacer(modifier = Modifier.height(8.dp))

            GoalProgress(
                currentAmount = currentAmount,
                targetAmount = targetAmount
            )
        }
    }
}