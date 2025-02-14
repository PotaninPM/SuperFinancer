package com.potaninpm.feature_finances.presentation.components.operations.section

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
import androidx.compose.ui.unit.dp
import com.potaninpm.core.functions.formatMoneySigned
import com.potaninpm.feature_finances.domain.model.Operation
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OperationItem(
    operation: Operation,
    icon: @Composable () -> Unit
) {
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
                text = operation.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = operation.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val amountColor = if (operation.amount >= 0) Color.Green else MaterialTheme.colorScheme.error

        Text(
            text = formatMoneySigned(operation.amount),
            style = MaterialTheme.typography.bodyLarge,
            color = amountColor.copy(alpha = 0.8f)
        )
    }
}