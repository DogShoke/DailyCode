package com.example.dailycode.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dailycode.News
import com.example.dailycode.NewsViewModel
import com.google.gson.Gson

@Composable
fun NewsScreen(navController: NavController, viewModel: NewsViewModel = viewModel()) {
    val allNews by viewModel.newsList
    val categories = listOf("Все", "Приложение", "Обновление", "Купоны")

    val selectedCategory = remember { mutableStateOf("Все") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadNews()
    }

    val filteredNews = if (selectedCategory.value == "Все") allNews else allNews.filter {
        it.category == selectedCategory.value
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
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

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                    ) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Фильтр")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = category,
                                            color =  if (selectedCategory.value == category)
                                                Color(0xFF4CAF50)// светло-зелёный фон
                                            else Color.Black

                                        )
                                    },
                                    onClick = {
                                        selectedCategory.value = category
                                        expanded = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                items(filteredNews.reversed()) { news ->
                    NewsItem(navController, news = news)
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
            .padding(top = 6.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            onClick = {
                val newsJson = Gson().toJson(news)
                navController.navigate("news_detail/${Uri.encode(newsJson)}")
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
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