package com.example.dailycode.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.News

@Composable
fun NewsDetailScreen(navController: NavController, news: News) {
    val context = LocalContext.current
    val imageResId = remember(news.image) {
        context.resources.getIdentifier(news.image, "drawable", context.packageName)
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp
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
                        "Детали новости",
                        fontWeight = FontWeight(700),
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(710.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Изображение новости
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Название новости по центру
                        Text(
                            text = news.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Строка с категорией слева и датой справа
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Категория слева
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF4CAF50),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = news.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }

                            // Дата справа
                            Text(
                                text = news.date,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.Gray)
                        Spacer(modifier = Modifier.height(20.dp))
                        // Полное описание новости
                        Text(
                            text = news.descriptionFull,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    )
}