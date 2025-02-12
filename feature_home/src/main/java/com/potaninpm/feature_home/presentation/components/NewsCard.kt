package com.potaninpm.feature_home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.domain.model.NewsArticle
import com.potaninpm.feature_home.R

@Composable
fun NewsCard(
    article: NewsArticle,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val painter = if (article.imageUrl.isNullOrEmpty()) {
        painterResource(id = R.drawable.error_24px)
    } else {
        rememberAsyncImagePainter(article.imageUrl)
    }

    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = article.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {

                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.abstract,
                maxLines = 3,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = article.source, style = MaterialTheme.typography.labelSmall)
                Text(text = article.publishedAt, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                }
            ) {
                Text(
                    text = "Открыть",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                )
            }
        }
    }
}
