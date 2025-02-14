package com.potaninpm.feature_finances.presentation.screens

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.domain.model.Operation
import com.potaninpm.feature_finances.presentation.components.goals.addGoalDialog.AddGoalDialog
import com.potaninpm.feature_finances.presentation.components.goals.goalCard.GoalCard
import com.potaninpm.feature_finances.presentation.components.operations.dialog.AddOperationDialog
import com.potaninpm.feature_finances.presentation.components.operations.section.OperationsSection
import com.potaninpm.feature_finances.presentation.viewModels.FinancesViewModel
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.components.financesCard.FinancesCard
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun groupOperationsByDate(operations: List<Operation>): Map<LocalDate, List<Operation>> {
    return operations.groupBy { operation ->
        Instant.ofEpochMilli(operation.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

@Composable
fun FinancesScreen(
    viewModel: FinancesViewModel = koinViewModel()
) {
    var showAddGoalDialog by rememberSaveable { mutableStateOf(false) }
    var showAddOperationDialog by rememberSaveable { mutableStateOf(false) }

    val onAddGoalClick = {
        showAddGoalDialog = true
    }

    val onAddOperationClick = {
        showAddOperationDialog = true
    }

    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onAddGoal = { title, targetAmount, currency, dueDate ->
                viewModel.addGoal(title, targetAmount, currency, dueDate)
                showAddGoalDialog = false
            }
        )
    }

    if (showAddOperationDialog) {
        val goals by viewModel.goals.collectAsState()

        AddOperationDialog(
            goals = goals,
            onDismiss = { showAddOperationDialog = false },
            onAddOperation = { goalId, amount, comment ->
                val goal = goals.find { it.id == goalId } ?: return@AddOperationDialog
                if (amount >= 0) {
                    viewModel.addDeposit(goal, amount, comment)
                } else {
                    viewModel.addWithdrawal(goal, -amount, comment)
                }
                showAddOperationDialog = false
            }
        )
    }

    FinancesScreenContent(
        viewModel = viewModel,
        onAddGoalClick = {
            onAddGoalClick()
        },
        onAddOperationClick = {
            onAddOperationClick()
        },
        onDeleteGoalClick = { goal ->
            viewModel.deleteGoal(goal)
        },
        onWithdrawClick = {
//            viewModel.addWithdrawal(
//
//            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinancesScreenContent(
    viewModel: FinancesViewModel,
    onAddGoalClick: () -> Unit,
    onAddOperationClick: () -> Unit,
    onDeleteGoalClick: (GoalEntity) -> Unit,
    onWithdrawClick: () -> Unit
) {
    val state = rememberScrollState()

    val goals by viewModel.goals.collectAsState()
    val operations by viewModel.operations.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val totalTarget by viewModel.totalTarget.collectAsState()
    val averageMonthlyInflow by viewModel.averageMonthlyInflow.collectAsState()

    val monthsToAchieve = (totalTarget - totalSavings) / averageMonthlyInflow
    val overallProgress = totalSavings.toFloat() / totalTarget.toFloat()

    Log.i("INFOG", overallProgress.toString())
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
            FinancesCard(
                totalSavings = totalSavings,
                totalTarget = totalTarget,
                averageMonthlyIncome = averageMonthlyInflow,
                monthsToAchieve = monthsToAchieve,
                overallProgress = overallProgress
            )

            YourGoalsSection(
                titleRes = R.string.goals,
                content = {
                    if (goals.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .alpha(0.7f),
                            text = "У вас пока нет целей",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        goals.forEach { goal ->
                            GoalCard(
                                title = goal.title,
                                dateOfReaching = goal.dueDate.toString(),
                                currentAmount = goal.currentAmount,
                                targetAmount = goal.targetAmount,
                                currency = goal.currency,
                                onDeleteClick = { viewModel.deleteGoal(goal) },
                                onWithdrawClick = {

                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                },
                onAddGoalClick = onAddGoalClick
            )

            HorizontalDivider(
                modifier = Modifier
            )

            OperationsSection(
                operations = operations,
                onAddOperationClick = onAddOperationClick
            )
        }
    }
}

@Composable
fun AddButton(
    onAddClick: () -> Unit,
    @StringRes title: Int,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable {
                onAddClick()
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

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            stringResource(title),
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun YourGoalsSection(
    @StringRes titleRes: Int,
    content: @Composable () -> Unit,
    onAddGoalClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                fontWeight = FontWeight.Bold,
            )

            AddButton(
                onAddClick = onAddGoalClick,
                title = R.string.add_goal
            )
        }
        content()
    }
}

