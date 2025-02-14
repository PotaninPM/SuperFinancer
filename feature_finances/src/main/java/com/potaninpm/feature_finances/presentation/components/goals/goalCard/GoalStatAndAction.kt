package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R

@Composable
fun GoalStatAndAction(
    currentAmount: Long,
    targetAmount: Long,
    onWithdrawClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
            targetAmount = targetAmount
        )
    }
}