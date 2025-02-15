package com.potaninpm.feature_finances.presentation.components.financesCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.components.CustomElevatedCard
import com.potaninpm.core.functions.formatMoneyUnsigned
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.components.goals.goalCard.GoalProgress

@Composable
fun FinancesCard(
    totalSavings: Long,
    totalTarget: Long,
    averageMonthlyIncome: Double,
    monthsToAchieve: Double,
    overallProgress: Float
) {
    CustomElevatedCard(
        modifier = Modifier
            .padding(top = 16.dp),
        background = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            FinancesCardItem(
                title = "${(overallProgress * 100).toInt()}%",
                subtitle = "Процент достижения целей",
                icon = {
                    GoalProgress(
                        modifier = Modifier
                            .size(43.dp),
                        currentAmount = totalSavings,
                        targetAmount = -1,
                        overallProgress = overallProgress,
                        insideThing = {
                            Icon(
                                painter = painterResource(R.drawable.account_balance_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            FinancesCardItem(
                title = "${formatMoneyUnsigned(totalTarget - totalSavings)} ₽",
                subtitle = "Осталось накопить",
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.error
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.savings_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = "${formatMoneyUnsigned(totalSavings)} ₽",
                subtitle = "Всего накоплено",
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.savings_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = "${formatMoneyUnsigned(averageMonthlyIncome.toLong())} ₽",
                subtitle = "Ежемесячный доход",
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icome_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = formatDuration(monthsToAchieve),
                subtitle = "Когда накопите",
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.schedule_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )
        }
    }
}

fun formatDuration(months: Double): String {
    return if (months < 1.0) {
        val days = (months * 30).toInt()
        "$days дней"
    } else if (months >= 1.0 && months < 12.0) {
        val formattedMonths = String.format("%.1f", months)
        "$formattedMonths месяцев"
    } else {
        val years = (months / 12).toInt()
        val remainingMonths = (months % 12).toInt()
        "$years лет $remainingMonths месяцев"
    }
}