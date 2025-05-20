package com.example.dailycode.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.CategoryDataStore
import kotlinx.coroutines.launch
import java.time.format.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(
    navController: NavController,
    onCategoriesSelected: (List<String>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedCategories = remember { mutableStateListOf<String>() }

    val availableCategories = listOf(
        "Еда",
        "Одежда",
        "Техника",
        "Красота",
        "Дом и ремонт",
        "Развлечения",
        "Спорт",
        "Транспорт",
        "Книги",
        "Путешествия"
    )

    LaunchedEffect(Unit) {
        val savedCategories = CategoryDataStore.loadCategories(context)
        selectedCategories.clear()
        if (savedCategories.isEmpty()) {
            selectedCategories.addAll(availableCategories)
        } else {
            selectedCategories.addAll(savedCategories)
        }
    }




    LaunchedEffect(Unit) {
        val savedCategories = CategoryDataStore.loadCategories(context)
        selectedCategories.clear()
        selectedCategories.addAll(savedCategories)
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
                    // Кнопка назад
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

                    // Заголовок
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
            Column(modifier = Modifier.padding(bottom = 100.dp)) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            CategoryDataStore.saveCategories(context, selectedCategories)
                            onCategoriesSelected(selectedCategories.toList())
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF34C924),
                        contentColor = Color.White
                    )
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
        LazyColumn(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 88.dp)
        ){
            items(availableCategories) { category ->
                val isSelected = category in selectedCategories
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isSelected) {
                                selectedCategories.remove(category)
                            } else {
                                selectedCategories.add(category)
                            }
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF34C924),
                            uncheckedColor = Color(0xFF34C924),
                            checkmarkColor = Color.White
                    )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(category,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
