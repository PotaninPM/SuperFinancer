package com.potaninpm.feature_finances.presentation.components.goals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.potaninpm.feature_finances.R

@Composable
fun GoalActionMenu(
    onDelete: () -> Unit,
    onWithdraw: () -> Unit,
    onTransfer: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
         Icon(
             painter = painterResource(id = R.drawable.more_vert_24px),
             contentDescription = stringResource(R.string.extra_actions),
             modifier = Modifier.clickable { expanded = true }
         )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDelete()
                },
                text = {
                    Text(stringResource(R.string.delete))
                }
            )
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onWithdraw()
                },
                text = {
                    Text(stringResource(R.string.withdraw))
                }
            )
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onTransfer()
                },
                text = {
                    Text(stringResource(R.string.transfer))
                }
            )
        }
    }
}