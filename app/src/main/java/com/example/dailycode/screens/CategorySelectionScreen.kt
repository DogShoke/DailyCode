package com.example.dailycode.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.CategoryDataStore
import kotlinx.coroutines.launch


@Composable
fun CategorySelectionScreen(
    navController: NavController,
    onCategoriesSelected: (List<String>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedCategories = remember { mutableStateListOf<String>() }
    val availableCategories = listOf(
        "Еда", "Одежда", "Техника", "Красота", "Дом и ремонт",
        "Развлечения", "Спорт", "Транспорт", "Книги", "Путешествия"
    )

    val wasLoaded = remember { mutableStateOf(false) }

    if (!wasLoaded.value) {
        LaunchedEffect(Unit) {
            val savedCategories = CategoryDataStore.loadCategories(context)
            selectedCategories.clear()
            if (savedCategories.isEmpty()) {
                selectedCategories.addAll(availableCategories)
            } else {
                selectedCategories.addAll(savedCategories)
            }
            wasLoaded.value = true
        }
    }


    BackHandler {
        coroutineScope.launch {
            CategoryDataStore.saveCategories(context, selectedCategories)
            navController.popBackStack()
        }
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
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                    Text(
                        "Выбор категорий",
                        fontWeight = FontWeight(700),
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            CategoryDataStore.saveCategories(context, selectedCategories)
                            onCategoriesSelected(selectedCategories.toList())
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .width(380.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7AC87A),
                        disabledContainerColor = Color.Gray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Text(
                        "Сохранить выбор",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            availableCategories.forEach { category ->
                val isSelected = category in selectedCategories
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                        .clickable {
                            if (isSelected) {
                                selectedCategories.remove(category)
                            } else {
                                selectedCategories.add(category)
                            }
                        },
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF7AC87A),
                                uncheckedColor = Color(0xFF7AC87A),
                                checkmarkColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = category,
                            fontSize = 18.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}