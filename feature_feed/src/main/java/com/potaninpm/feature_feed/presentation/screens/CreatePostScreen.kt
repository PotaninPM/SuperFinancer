package com.potaninpm.feature_feed.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.core.ui.screens.FullScreenImageDialog
import com.potaninpm.core.ui.components.AddButton
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_feed.R
import com.potaninpm.feature_feed.domain.model.TagTypes
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

    val allTags = listOf(
        TagTypes(stringResource(id = R.string.business), "business"),
        TagTypes(stringResource(id = R.string.finances), "finances"),
        TagTypes(stringResource(id = R.string.entertainment), "entertainment"),
        TagTypes(stringResource(id = R.string.investments), "investments"),
        TagTypes(stringResource(id = R.string.news), "news")
    )

    val postCreated = stringResource(R.string.post_created)

    var fullScreenImage by remember { mutableStateOf<ByteArray?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val stream = context.contentResolver.openInputStream(it)
            val bytes = stream?.readBytes()
            stream?.close()
            if (bytes != null && photoBytesList.size < 2) {
                photoBytesList = photoBytesList + bytes
            } else {
                Toast.makeText(context, context.getString(R.string.max_photos), Toast.LENGTH_SHORT).show()
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
                title = { Text(stringResource(R.string.create_post)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(stringResource(R.string.create_post))
                },
                icon = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.create_post))
                },
                onClick = {
                    if (postText.isNotBlank() && tagsSelected.isNotEmpty()) {
                        viewModel.addPost(
                            webUrl = selectedUrl,
                            text = postText,
                            imageData = photoBytesList,
                            tags = tagsSelected
                        )
                        Toast.makeText(context, postCreated, Toast.LENGTH_SHORT).show()
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
            CustomTextField(
                value = postText,
                onValueChange = { postText = it },
                enabled = true,
                isError = false,
                maxLines = 5,
                error = null,
                hint = stringResource(R.string.enter_post_text)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(stringResource(R.string.choose_tags))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(allTags) { tag ->
                    FilterChip(
                        selected = tag.tagType in tagsSelected,
                        onClick = {
                            tagsSelected = if (tag.tagType in tagsSelected) {
                                tagsSelected - tag.tagType
                            } else {
                                tagsSelected + tag.tagType
                            }
                        },
                        label = { Text(tag.tagName) }
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
                itemsIndexed(photoBytesList) { index, bytes ->
                    PhotoItem(
                        photoBytes = bytes,
                        onDeleteClick = {
                            photoBytesList = photoBytesList.toMutableList().apply { removeAt(index) }
                        },
                        onPhotoClick = {
                            fullScreenImage = bytes
                        }
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

@Composable
fun PhotoItem(
    photoBytes: ByteArray,
    onDeleteClick: () -> Unit,
    onPhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(140.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = photoBytes),
            contentDescription = stringResource(R.string.photo),
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onPhotoClick() }
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                    shape = CircleShape
                )

        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.hide),
                tint = Color.Red
            )
        }
    }
}
