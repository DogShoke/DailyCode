package com.example.dailycode.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycode.News
import com.example.dailycode.NewsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson


@Composable
fun NewsScreen(navController: NavController, viewModel: NewsViewModel = viewModel()) {
    val newsList by viewModel.newsList
    LaunchedEffect(Unit) {
        viewModel.loadNews()
    }
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
            color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                    Text(
                        "Новости",
                        fontWeight = FontWeight(700),
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues).padding(bottom = 80.dp)) {
                items(newsList.reversed()) { news ->
                    NewsItem(navController, news = news)
                    //Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    )
}



@Composable
fun NewsItem(navController: NavController, news: News) {
    val context = LocalContext.current
    val imageResId = remember(news.image) {
        context.resources.getIdentifier(news.image, "drawable", context.packageName)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 6.dp)
        ,
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            onClick = {
                val newsJson = Gson().toJson(news)
                navController.navigate("news_detail/${Uri.encode(newsJson)}")
            },
            modifier = Modifier
                .padding(8.dp)
                .width(360.dp)
                .height(335.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Text(text = news.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = news.date, style = MaterialTheme.typography.labelSmall)
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF4CAF50),
                            shape = RoundedCornerShape(50)
                        )
                        .background(color = Color.White, shape = RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = news.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = news.descriptionPreview)
            }
        }
    }
}