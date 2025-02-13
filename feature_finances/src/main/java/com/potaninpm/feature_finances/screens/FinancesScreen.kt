package com.potaninpm.feature_finances.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.components.financesCard.FinancesCard
import com.potaninpm.feature_finances.components.goals.addGoalDialog.AddGoalDialog
import com.potaninpm.feature_finances.components.goals.goalCard.GoalCard
import com.potaninpm.feature_finances.components.operations.OperationsSection
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Operation(
    val date: Long,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val comment: String? = null
)

fun groupOperationsByDate(operations: List<Operation>): Map<LocalDate, List<Operation>> {
    return operations.groupBy { operation ->
        Instant.ofEpochMilli(operation.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}


@Composable
fun FinancesScreen() {
    var showAddGoalDialog by rememberSaveable { mutableStateOf(false) }

    val onAddGoalClick = {
        showAddGoalDialog = true
    }

    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = {
                showAddGoalDialog = false
            },
            onAddGoal = { name, targetAmount, dueDate ->
                showAddGoalDialog = false
            }
        )
    }

    FinancesScreenContent(
        onAddGoalClick = {
            onAddGoalClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinancesScreenContent(
    onAddGoalClick: () -> Unit
) {
    val state = rememberScrollState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.finances),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                )
                HorizontalDivider()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp)
                .verticalScroll(state)
        ) {
            FinancesCard()

            YourGoalsSection(
                titleRes = R.string.goals,
            ) {
                for (i in 1..5) {
                    GoalCard(
                        title = "Накопить на квартиру",
                        currentAmount = 1000 * 2 * i.toLong(),
                        targetAmount = 10000,
                        onDeleteClick = {

                        },
                        onWithdrawClick = {

                        }
                    )
                }

                AddGoalButton(
                    onAddGoalClick = onAddGoalClick
                )
            }

            HorizontalDivider(
                modifier = Modifier
            )

            OperationsSection(
                sampleOperations
            )
        }
    }
}

val sampleOperations = listOf(
    Operation(
        date = 1698307200000,
        title = "Снятие денег",
        subtitle = "На квартиру",
        amount = -95.0
    ),
    Operation(
        date = 1678307200003,
        title = "Снятие денег",
        subtitle = "На квартиру",
        amount = -22165.0,
    ),
    Operation(
        date = 1678307200002,
        title = "Пополнение",
        subtitle = "На машину",
        amount = 1500.0,
    ),
    Operation(
        date = 1678307200001,
        title = "Снятие денег",
        subtitle = "На квартиру",
        amount = -61.76,
    ),
    Operation(
        date = 1678307200000,
        title = "Пополнение",
        subtitle = "На квартиру",
        amount = 20592.0,
        comment = "На кормление рыбок"
    )
)


@Composable
fun AddGoalButton(
    onAddGoalClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(vertical = 12.dp)
            .clickable {
                onAddGoalClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            stringResource(R.string.add_goal),
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun YourGoalsSection(
    @StringRes titleRes: Int,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                fontWeight = FontWeight.Bold,
                modifier = Modifier

            )
        }
        content()
    }
}

