package com.potaninpm.feature_home.presentation.components.searchBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun FakeSearchBar(
    onSearchClick: () -> Unit
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    SearchBar(
        query = searchText,
        onQueryChange = {
            searchText = it
        },
        onClick = onSearchClick
    )
}
