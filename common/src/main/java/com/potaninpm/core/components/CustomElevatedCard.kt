package com.potaninpm.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val backgroundColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.inverseOnSurface
    } else {
        MaterialTheme.colorScheme.background
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults
            .elevatedCardColors(
                containerColor = backgroundColor
            )
    ) {
        content()
    }
}

