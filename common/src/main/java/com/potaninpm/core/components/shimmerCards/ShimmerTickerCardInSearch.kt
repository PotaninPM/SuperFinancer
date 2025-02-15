package com.potaninpm.core.components.shimmerCards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.core.components.shimmerLoading

@Composable
fun ShimmerTickerCardInSearch() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerLoading(2000)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AAPL",
                    modifier = Modifier
                        .shimmerLoading(2000),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                Image(
                    imageVector = Icons.Default.Refresh,
                    modifier = Modifier
                        .shimmerLoading(2000),
                    contentDescription = null,
                )
            }

                Text(
                    modifier = Modifier
                        .widthIn(max = 170.dp)
                        .shimmerLoading(2000),
                    maxLines = 1,
                    text = "Apple Inc.",
                    style = MaterialTheme.typography.bodySmall
                )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "123.45",
                modifier = Modifier
                    .shimmerLoading(2000),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = "+0.12 (0.12%)",
                color = Color.Gray,
                modifier = Modifier
                    .shimmerLoading(2000),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}