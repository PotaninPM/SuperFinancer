package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.core.functions.formatMoneyUnsigned
import com.potaninpm.feature_finances.R

@Composable
fun GoalTextInfo(
    title: String,
    dateOfReaching: String,
    currentAmount: Long,
    targetAmount: Long,
    currency: String
) {
    val formattedCurrentAmount = formatMoneyUnsigned(currentAmount)
    val formattedTargetAmount = formatMoneyUnsigned(targetAmount)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "$formattedCurrentAmount $currency из $formattedTargetAmount $currency",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            if (dateOfReaching != "null") {
                Text(
                    text = "До $dateOfReaching",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = stringResource(R.string.without_date),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}