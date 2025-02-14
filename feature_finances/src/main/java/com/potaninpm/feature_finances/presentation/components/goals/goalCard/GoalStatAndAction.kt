package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
fun GoalStatAndAction(
    currentAmount: Long,
    targetAmount: Long,
    onWithdrawClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val progress =  if (targetAmount > 0) {
        currentAmount.toFloat() / targetAmount.toFloat()
    } else 0f

    val progressColor = when {
        progress < 0.25f -> Color.Red.copy(alpha = 1f)
        progress < 0.5f -> Color(255, 165, 0).copy(alpha = progress + 0.2f)
        progress < 0.95f -> Color(255, 215, 0).copy(alpha = progress + 0.1f)
        progress != 1f -> Color.Green.copy(alpha = progress - 0.3f)
        else -> Color.Green
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.more_vert_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable {

                }
        )

        GoalProgress(
            currentAmount = currentAmount,
            targetAmount = targetAmount,
            insideThing = {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = progressColor
                )
            }
        )
    }
}