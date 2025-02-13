package com.potaninpm.feature_finances.components.goals.goalCard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.R

@Composable
fun GoalFooter(
    onWithdrawClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = {
                onDeleteClick()
            },
            modifier = Modifier
        ) {
            Text(text = stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            onClick = {
                onWithdrawClick()
            },
            modifier = Modifier
        ) {
            Text(text = stringResource(R.string.withdraw))
        }



//        Image(
//            painter = painterResource(R.drawable.delete_24px),
//            contentDescription = null,
//            modifier = Modifier
//                .clickable {
//                    onDeleteClick()
//                }
//        )
    }
}