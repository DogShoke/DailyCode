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
import com.example.dailycode.data.Coupon
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarRow(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    today: LocalDate
) {
    val dates = remember {
        List(14) { i -> Day(today.minusDays(7).plusDays(i.toLong())
        ) }
    }

    val listState = rememberLazyListState()
    val todayIndex = dates.indexOfFirst { it.date == today }

    LaunchedEffect(Unit) {
        listState.scrollToItem(todayIndex)
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
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
        isSelected -> Color(0xFF7AC87A)
        isToday -> Color(0xFFBDEDBD)
        else -> Color.LightGray
    }
    val borderColor = Color.Transparent
    val dayOfWeek = day.date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale("ru"))
        .uppercase()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .size(60.dp)
            .clickable { onClick() }
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(2.dp, borderColor, CircleShape)
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                fontSize = 16.sp,
                color = Color.White

            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dayOfWeek,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }
}







