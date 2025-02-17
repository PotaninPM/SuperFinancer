package com.potaninpm.feature_feed.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.potaninpm.feature_feed.R
import com.potaninpm.feature_feed.presentation.viewModels.PostsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavHostController,
    selectedUrl: String = "",
    onPostCreated: (String) -> Unit = {},
    viewModel: PostsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    var postText by rememberSaveable { mutableStateOf("") }
    var tagsSelected by remember { mutableStateOf<List<String>>(emptyList()) }
    var photoBytesList by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    val allTags = listOf("Бизнес", "Финансы", "Развлечения", "Инвестиции", "Новости")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создать пост") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (postText.isNotBlank() && tagsSelected.isNotEmpty()) {
                        viewModel.addPost(
                            webUrl = selectedUrl,
                            text = postText,
                            imageData = photoBytesList,
                            tags = tagsSelected
                        )

                        Toast.makeText(context,
                            context.getString(R.string.post_created), Toast.LENGTH_SHORT).show()
                        onPostCreated(postText)
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Создать")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = postText,
                onValueChange = { postText = it },
                label = { Text("Текст поста") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Выберите тэги (минимум 1)")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(allTags) { tag ->
                    FilterChip(
                        selected = tag in tagsSelected,
                        onClick = {
                            tagsSelected = if (tag in tagsSelected) {
                                tagsSelected - tag
                            } else {
                                tagsSelected + tag
                            }
                        },
                        label = { Text(tag) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Прикрепите фото (от 0 до N)")
            Button(onClick = {
                val dummyImage = ByteArray(100) { (0..255).random().toByte() }
                photoBytesList = photoBytesList + dummyImage
            }) {
                Text("Добавить фото")
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(photoBytesList) { bytes ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Фото", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

