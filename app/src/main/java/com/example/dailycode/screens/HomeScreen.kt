package com.example.dailycode.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.CalendarRow
import com.example.dailycode.CategoryDataStore
import com.example.dailycode.CouponHistoryDataStore
import com.example.dailycode.FirebaseCouponRepository
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val claimedCouponDao = remember { db.claimedCouponDao() }

    val couponRepository = remember { FirebaseCouponRepository() }

    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }

    var selectedCategories by remember {
        mutableStateOf(
            listOf(
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
        )
    }
    var coupon by remember { mutableStateOf<Coupon?>(null) }
    var categoriesLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val savedCategories = CategoryDataStore.loadCategories(context)
        selectedCategories = if (savedCategories.isNotEmpty()) {
            savedCategories
        } else {
            listOf(
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
        }
        categoriesLoaded = true


        val todayStr = today.toString()
        val savedCoupon = CouponHistoryDataStore.getCouponForDate(context, todayStr)
        if (savedCoupon != null) {
            coupon = savedCoupon
        } else {
            val claimedCouponIds = claimedCouponDao.getAllClaimedCoupons().first().map { it.id }
            val newCoupon =
                couponRepository.getRandomCouponByCategories(selectedCategories, claimedCouponIds)
            if (newCoupon != null) {
                CouponHistoryDataStore.saveCouponForDate(context, todayStr, newCoupon)
                coupon = newCoupon
            }
        }
    }


    LaunchedEffect(selectedDate) {
        if (!categoriesLoaded) return@LaunchedEffect
        val dateStr = selectedDate.toString()
        val savedCoupon = CouponHistoryDataStore.getCouponForDate(context, dateStr)
        coupon = savedCoupon
    }

    var isClaimed by remember { mutableStateOf(false) }

    LaunchedEffect(coupon) {
        if (coupon != null) {
            val claimedCoupons = claimedCouponDao.getAllClaimedCoupons().first()
            isClaimed = claimedCoupons.any { it.id == coupon!!.id }
        } else {
            isClaimed = false
        }
    }

    SwipeableHomeScreen(
        selectedDate = selectedDate,
        onDateChange = { newDate ->
            selectedDate = newDate
        },
        contentForDate = { date ->
        Column(modifier = Modifier.padding(16.dp)) {
            CalendarRow(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                today = today
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                selectedDate.isAfter(today) -> {
                    Text("Купон пока недоступен. Дождитесь нужной даты.")
                }

                coupon != null -> {
                    CouponItem(coupon!!)
                }

                else -> {
                    Text("Нет купонов для выбранной даты")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val isButtonEnabled = selectedDate == today && coupon != null && !isClaimed
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF34C924),
                        disabledContainerColor = Color.Gray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    ),
                    onClick = {
                        coupon?.let {
                            coroutineScope.launch {
                                couponRepository.claimCouponLocally(context, it)
                                if (coupon != null) {
                                    val claimedCoupons =
                                        claimedCouponDao.getAllClaimedCoupons().first()
                                    isClaimed = claimedCoupons.any { it.id == coupon!!.id }
                                } else {
                                    isClaimed = false
                                }
                            }

                        }
                    },
                    enabled = isButtonEnabled

                ) {
                    Text(
                        "Забрать",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HomeInfoCard(
                        modifier = Modifier.weight(1f).height(150.dp),
                        title = "Выбрать категории",
                        description = "Купоны выдаются в зависимости от выбранных категорий",
                        onClick = { navController.navigate("select_categories") }
                    )

                    HomeInfoCard(
                        modifier = Modifier.weight(1f).height(150.dp),
                        title = "Мои карты",
                        description = "Сохраненные карты магазинов для удобного доступа",
                        onClick = { navController.navigate("cards") }
                    )
                }
            }
        }
    })
}

@Composable
fun CouponItem(coupon: Coupon) {
    val context = LocalContext.current
    val imageResId = remember(coupon.imageUrl) {
        context.resources.getIdentifier(coupon.imageUrl, "drawable", context.packageName)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${coupon.storeName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = coupon.category.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = coupon.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwipeableHomeScreen(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    contentForDate: @Composable (LocalDate) -> Unit
) {
    val swipeThreshold = 100.dp
    val density = LocalDensity.current
    val thresholdPx = with(density) { swipeThreshold.toPx() }

    var dragOffset by remember { mutableStateOf(0f) }
    var isSwiping by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(selectedDate) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isSwiping = true
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        dragOffset += dragAmount
                    },
                    onDragEnd = {
                        isSwiping = false
                        when {
                            dragOffset > thresholdPx -> {
                                onDateChange(selectedDate.minusDays(1))
                            }
                            dragOffset < -thresholdPx -> {
                                onDateChange(selectedDate.plusDays(1))
                            }
                        }
                        dragOffset = 0f
                    }
                )
            }
    ) {
        contentForDate(selectedDate)
    }
}


@Composable
fun HomeInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp)
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}



