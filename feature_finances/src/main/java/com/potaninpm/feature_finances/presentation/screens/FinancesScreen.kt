package com.potaninpm.feature_finances.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.components.FinancesCard
import com.potaninpm.feature_finances.presentation.components.goals.GoalCard

@Composable
fun FinancesScreen() {
    FinancesScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinancesScreenContent() {
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
                .verticalScroll(state)
        ) {
            FinancesCard()

            Spacer(modifier = Modifier.height(12.dp))

            YourGoalsSection(
                titleRes = R.string.goals,
            ) {

            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            for (i in 1..11) {
                GoalCard(
                    title = "Накопить на квартиру",
                    currentAmount = 1000 * i.toLong(),
                    targetAmount = 10000
                )
            }
        }
    }
}

@Composable
fun YourGoalsSection(
    @StringRes titleRes: Int,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            fontWeight = FontWeight.Bold,
            modifier = Modifier

        )
        content()
    }
}

