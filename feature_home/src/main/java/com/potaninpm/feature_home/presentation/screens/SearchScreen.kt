package com.potaninpm.feature_home.presentation.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.presentation.components.NewsCard
import com.potaninpm.feature_home.presentation.viewModels.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val focusRequester = remember { FocusRequester() }

    val query by searchViewModel.query.collectAsState()
    val results by searchViewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    SearchScreenContent(
        query = query,
        onQueryChange = { newQuery ->
            searchViewModel.setQuery(newQuery)
        },
        focusRequester = focusRequester,
    )

    //Log.d("INFOG", "SearchScreen: results: ${results}")

    if (results.news.isNotEmpty()) {
        Text("Новости", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(results.news) { article ->
                NewsCard(article = article, onClick = {

                })
            }
        }
    }
}

@Composable
private fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var searchText by rememberSaveable {
        mutableStateOf(query)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
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
            placeholder = { Text("Search games") },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .focusRequester(focusRequester)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.medium
                ),
            trailingIcon = {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(painter = painterResource(id = R.drawable.arrow_drop_up_24px), contentDescription = null)
                }
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        )
    }
}

