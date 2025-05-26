package com.example.dailycode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = FirebaseNewsRepository()

    private val _newsList = mutableStateOf<List<News>>(emptyList())
    val newsList: State<List<News>> = _newsList

    fun loadNews() {
        viewModelScope.launch {
            val result = repository.getAllNews()
            _newsList.value = result
        }
    }


    fun getNewsById(id: String): News? {
        return newsList.value.find { it.id == id }
    }

}
