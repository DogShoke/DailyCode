package com.example.dailycode

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarRow(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.now() }

    // 38 дней: 7 до, 30 после
    val dates = remember {
        List(38) { i -> Day(
            today.minusDays(7).plusDays(i.toLong())
        ) }
    }

    val listState = rememberLazyListState()

    val todayIndex = dates.indexOfFirst { it.date == today }

    LaunchedEffect(Unit) {
        listState.scrollToItem(todayIndex)
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        items(dates) { day ->
            DayItem(
                day = day,
                isToday = day.date == today,
                isSelected = day.date == selectedDate,
                onClick = { onDateSelected(day.date) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayItem(day: Day, isToday: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        isSelected -> Color.Gray
        else -> Color.LightGray
    }
    val borderColor = if (isToday) Color.Blue else Color.Transparent

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .size(50.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(2.dp, borderColor, CircleShape)
            .clickable { onClick() }
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            fontSize = 16.sp,
            color = Color.White
        )
    }
}







