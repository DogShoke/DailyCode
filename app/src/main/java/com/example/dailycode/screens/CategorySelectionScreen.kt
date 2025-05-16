package com.example.dailycode.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailycode.CategoryDataStore
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(
    navController: NavController,
    onCategoriesSelected: (List<String>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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

    val selectedCategories = remember { mutableStateListOf<String>() }

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
            TopAppBar(
                title = { Text("Выбор категорий") }
            )
        },
        bottomBar = {

        }
    ) { padding ->
        LazyColumn(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)) {
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
                        onCheckedChange = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(category)
                }
            }
            item {
                val coroutineScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        coroutineScope.launch {
                            // Сохраняем выбранные категории в DataStore
                            CategoryDataStore.saveCategories(context, selectedCategories)
                            // Передаем выбранные категории назад и закрываем экран
                            onCategoriesSelected(selectedCategories.toList())
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить выбор")
                }
            }

        }
    }
}
