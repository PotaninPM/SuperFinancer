package com.potaninpm.feature_home.presentation.components.searchBar

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_home.R
import kotlinx.coroutines.delay

@Composable
fun SearchBar(
    onMicClick: () -> Unit,
    onClear: () -> Unit,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(searchText) {
        delay(500)

        if (searchText.isEmpty()) {
            onClear()
        } else {
            onQueryChange(searchText)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onQueryChange(it)
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = Color.White
            ),
            placeholder = { Text("Search...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .focusRequester(focusRequester)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.medium
                ),
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    Text("Clear",
                        modifier = Modifier
                            .clickable {
                                searchText = ""
                                onQueryChange("")
                                onClear()
                            }
                    )
                } else {
                    IconButton(
                        onClick = {
                            onMicClick()
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.mic_24px), contentDescription = null)
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        )
    }
}