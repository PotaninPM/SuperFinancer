package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GoalTextInfo(
    title: String,
    dateOfReaching: String,
    currentAmount: Long,
    targetAmount: Long,
    currency: String
) {
    val formattedCurrentAmount = formatMoney(currentAmount)
    val formattedTargetAmount = formatMoney(targetAmount)

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
                    text = "Без срока",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

fun formatMoney(value: Long): String {
    val numberFormat = NumberFormat.getInstance(Locale("ru", "RU"))
    val formattedValue = numberFormat.format(value)

    return formattedValue
}