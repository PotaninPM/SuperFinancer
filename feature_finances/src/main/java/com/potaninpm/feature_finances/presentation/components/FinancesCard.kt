package com.potaninpm.feature_finances.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R

@Composable
fun FinancesCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = 12.dp.let { CardDefaults.cardElevation(it, it, it, it, it, it) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            FinancesCardItem(
                title = "345.6 ₽",
                subtitle = "Всего накоплено",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.savings_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            FinancesCardItem(
                title = "3000 ₽ / месяц",
                subtitle = "Поступления",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.icome_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = "Через 2 года",
                subtitle = "Когда накопите",
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.schedule_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            )
        }
    }
}