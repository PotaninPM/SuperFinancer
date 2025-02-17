package com.potaninpm.feature_finances.presentation.components.operations.section

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.functions.formatMoneySigned
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.domain.model.Operation
import com.potaninpm.feature_finances.presentation.components.operations.CommentSection
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OperationItem(
    operation: Operation,
    icon: @Composable () -> Unit
) {
    val operationType = when (operation.type) {
        "deposit" -> stringResource(R.string.income)
        "withdrawal" -> "Снятие"
        "transfer_in" -> "Перевод"
        else -> "???"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(43.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = operationType,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = operation.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val amountColor = if (operation.amount >= 0) Color(0xFF059300) else MaterialTheme.colorScheme.error

        Text(
            text = formatMoneySigned(operation.amount, operation.currency),
            style = MaterialTheme.typography.bodyLarge,
            color = amountColor
        )
    }
    operation.comment?.let { CommentSection(comment = it) }
}

