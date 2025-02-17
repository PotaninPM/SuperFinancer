package com.potaninpm.feature_feed.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.core.ui.FullScreenImageDialog
import com.potaninpm.core.ui.components.AddButton
import com.potaninpm.core.ui.components.CustomTextField
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
    var postText by rememberSaveable { mutableStateOf("") }
    var tagsSelected by remember { mutableStateOf<List<String>>(emptyList()) }
    var photoBytesList by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    val allTags = listOf("Бизнес", "Финансы", "Развлечения", "Инвестиции", "Новости")
    var fullScreenImage by remember { mutableStateOf<ByteArray?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val stream = context.contentResolver.openInputStream(it)
            val bytes = stream?.readBytes()
            stream?.close()
            if (bytes != null) {
                photoBytesList = photoBytesList + bytes
            }
        }
    }

    if (fullScreenImage != null) {
        FullScreenImageDialog(
            imageBytes = fullScreenImage!!,
            onDismiss = { fullScreenImage = null }
        )
    }

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
            ExtendedFloatingActionButton(
                text = {
                    Text("Создать пост")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Создать пост")
                },
                onClick = {
                    if (postText.isNotBlank() && tagsSelected.isNotEmpty()) {
                        viewModel.addPost(
                            webUrl = selectedUrl,
                            text = postText,
                            imageData = photoBytesList,
                            tags = tagsSelected
                        )
                        onPostCreated(postText)
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
//            OutlinedTextField(
//                value = postText,
//                maxLines = 5,
//                onValueChange = { postText = it },
//                label = { Text("Текст поста") },
//                modifier = Modifier.fillMaxWidth()
//            )
            CustomTextField(
                value = postText,
                onValueChange = { postText = it },
                enabled = true,
                isError = false,
                maxLines = 5,
                error = null,
                hint = "Введите текст поста"
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

            Text(stringResource(R.string.attach_photos))

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(photoBytesList) { bytes ->
                    Image(
                        painter = rememberAsyncImagePainter(model = bytes),
                        contentDescription = "Фото",
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { fullScreenImage = bytes }
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AddButton(
                onAddClick = {
                    imagePickerLauncher.launch("image/*")
                },
                title = R.string.add_photo
            )
        }
    }
}

