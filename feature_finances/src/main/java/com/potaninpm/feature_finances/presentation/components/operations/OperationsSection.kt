package com.potaninpm.feature_finances.presentation.components.operations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.screens.Operation
import com.potaninpm.feature_finances.presentation.screens.groupOperationsByDate

@Composable
fun OperationsSection(
    operations: List<Operation>
) {
    val grouped = groupOperationsByDate(operations)
        .toList()
        .sortedByDescending { it.first }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        grouped.forEach { (date, ops) ->
            DateHeader(date = date)

            ops.forEach { operation ->
                OperationItem(
                    operation = operation,
                    icon = {
                        if (operation.amount >= 0) {
                            Icon(
                                painter = painterResource(R.drawable.icome_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.delete_24px),
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
}
