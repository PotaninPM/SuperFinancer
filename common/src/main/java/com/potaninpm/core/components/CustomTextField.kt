package com.potaninpm.core.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    hint: String,
    type: String?,
    enabled: Boolean = true,
    isError: Boolean,
    error: String?,
    onValueChange: (String) -> Unit
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(hint) },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                    shape = MaterialTheme.shapes.medium
                ).clickable(
                    onClick = {
                        Log.i("CustomTextField", "onClick")
                    }
                ),
            singleLine = true,
            isError = isError,
            keyboardOptions = if (type == "number") {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            } else {
                KeyboardOptions.Default
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary
            )
        )

        if (isError && error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}