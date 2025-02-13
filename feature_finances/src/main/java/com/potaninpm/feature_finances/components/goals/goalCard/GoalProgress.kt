package com.potaninpm.feature_finances.components.goals.goalCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R

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
        progress < 0.95f -> Color(255, 215, 0).copy(alpha = progress + 0.1f)
        progress != 1f -> Color.Green.copy(alpha = progress - 0.3f)
        else -> Color.Green
    }

    val painter = if (progress == 1f) {
        painterResource(id = R.drawable.check_24px)
    } else {
        painterResource(id = R.drawable.trophy_24px)
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .size(55.dp)
        )

        if (progress == 1f) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        } else {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = progressColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}