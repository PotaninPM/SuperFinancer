package com.potaninpm.feature_home.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchText = remember { mutableStateOf("") }
    var active = remember { mutableStateOf(false) }

    val colors1 = SearchBarDefaults.colors()

    androidx.compose.material3.SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchText.value,
                onQueryChange = { searchText.value = it },
                onSearch = {
                    active.value = false
                },
                expanded = active.value,
                onExpandedChange = { active.value = it },
                enabled = true,
                placeholder = { Text("Поиск...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Иконка поиска"
                    )
                },
                trailingIcon = null,
                interactionSource = null,
            )
        },
        expanded = active.value,
        onExpandedChange = { active.value = it },
        modifier = Modifier,
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors1,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        content = {
            Text("Content")
        }
    )
}