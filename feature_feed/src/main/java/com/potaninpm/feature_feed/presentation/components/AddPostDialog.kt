package com.potaninpm.feature_feed.presentation.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddPostDialog(
    onDismiss: () -> Unit,
    onCreatePost: (String, List<ByteArray>, List<String>) -> Unit
) {
    var postText by rememberSaveable { mutableStateOf("") }
    var tagsSelected by remember { mutableStateOf<List<String>>(emptyList()) }
    var photoBytesList by remember { mutableStateOf<List<ByteArray>>(emptyList()) }

    val allTags = listOf("Бизнес", "Финансы", "Развлечения", "Инвестиции", "Новости")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать пост") },
        text = {
            Column {
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
                Button(
                    onClick = {
                        val dummyImage = ByteArray(100) { (0..255).random().toByte() }
                        photoBytesList = photoBytesList + dummyImage
                    }
                ) {
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
        },
        confirmButton = {
            Button(onClick = {
                if (postText.isNotBlank() && tagsSelected.isNotEmpty()) {
                    onCreatePost(postText, photoBytesList, tagsSelected)
                }
                onDismiss()
            }) {
                Text("Создать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


