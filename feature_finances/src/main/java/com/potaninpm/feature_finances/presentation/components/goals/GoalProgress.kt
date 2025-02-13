package com.potaninpm.feature_finances.presentation.components.goals

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GoalProgress(
    currentAmount: Long,
    targetAmount: Long
) {
    val progress = if (targetAmount > 0) {
        currentAmount.toFloat() / targetAmount.toFloat()
    } else 0f

    val progressColor = when {
        progress < 0.25f -> Color.Red.copy(alpha = 1f)
        progress < 0.5f -> Color(255, 165, 0).copy(alpha = progress + 0.2f)
        progress < 0.95f -> Color(255, 255, 0).copy(alpha = progress + 0.1f)
        progress != 1f -> Color.Green.copy(alpha = progress - 0.3f)
        else -> Color.Green
    }

    LinearProgressIndicator(
        progress = { progress },
        color = progressColor,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
    )
}