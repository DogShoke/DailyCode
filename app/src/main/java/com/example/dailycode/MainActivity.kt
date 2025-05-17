package com.example.dailycode


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.preferencesDataStore
import com.example.dailycode.bottom_navigation.MainScreen
import com.example.dailycode.data.Coupon
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val coupons = listOf(
            Coupon("ДНС", "Техника", "Скидка 20% на все товары", "dns", "dns_tech_1", true, 1.0),
            Coupon("samokat", "Еда", "Бесплатная кола", "samokat", "samokat_food_1", true, 1.0),
            Coupon("yandex", "Музыка", "Яндекс плюс на 10 дней", "yandex", "yandex_music_1", true, 1.0),
            Coupon("OZON", "Книги", "Скидка 30% на бумажные книги", "test", "ozon_books_30", true, 1.0),
            Coupon("Wildberries", "Одежда", "Скидка 25% на летнюю коллекцию", "test", "wildberries_clothes_1", true, 1.0),
            Coupon("Перекрёсток", "Продукты", "Скидка 10% на все свежие фрукты", "test", "perekrestok_groceries_1", true, 1.0),
            Coupon("AliExpress", "Гаджеты", "Кэшбэк 15% на заказы от 2000₽", "test", "aliexpress_gadgets_1", true, 1.0),
            Coupon("М.Видео", "Электроника", "Промокод на 500₽ при покупке от 5000₽", "test", "mvideo_electronics_1", true, 1.0),
            Coupon("Delivery Club", "Еда", "Бесплатная доставка на первый заказ", "test", "deliveryclub_food_1", true, 1.0),
            Coupon("H&M", "Одежда", "Скидка 20% на мужскую коллекцию", "test", "hm_clothes_1", true, 1.0),
            Coupon("IKEA", "Дом", "Подарок при покупке от 3000₽", "test", "ikea_home_1", true, 1.0),
            Coupon("Пятёрочка", "Продукты", "2 по цене 1 на молочные продукты", "test", "pyaterochka_groceries_1", true, 1.0),
            Coupon("Avito", "Онлайн", "Скидка 100₽ на продвижение объявления", "test", "avito_online_1", true, 1.0),
            Coupon("Читай-город", "Книги", "Купон на 200₽ при покупке от 1000₽", "test", "chitay_books_1", true, 1.0),
            Coupon("Lamoda", "Обувь", "Скидка 15% на весь ассортимент", "test", "lamoda_shoes_1", true, 1.0),
            Coupon("Spotify", "Музыка", "Премиум на 1 месяц бесплатно", "test", "spotify_music_1", true, 1.0),
            Coupon("Азбука Вкуса", "Продукты", "Промокод на 300₽ при заказе от 2000₽", "test", "azbuka_groceries_1", true, 1.0),
            Coupon("DNS", "Техника", "Скидка 10% на аксессуары", "test", "dns_accessories_1", true, 1.0),
            Coupon("Reebok", "Спорт", "Скидка 20% на все кроссовки", "test", "reebok_sport_1", true, 1.0),
            Coupon("Магнит", "Продукты", "Промокод на скидку 5% в приложении", "test", "magnit_groceries_1", true, 1.0),
            Coupon("Nike", "Спорт", "Скидка 25% на новую коллекцию", "test", "nike_sport_1", true, 1.0),
            Coupon("ЛитРес", "Книги", "Электронная книга в подарок", "test", "litres_books_1", true, 1.0),
            Coupon("Adidas", "Одежда", "Промокод на 1000₽ при покупке от 5000₽", "test", "adidas_clothes_1", true, 1.0),
            Coupon("Мегафон", "Связь", "10 ГБ интернета бесплатно", "test", "megafon_mobile_1", true, 1.0),
            Coupon("Бургер Кинг", "Еда", "2 бургера по цене одного", "test", "burgerking_food_1", true, 1.0),
            Coupon("Тануки", "Еда", "Сет роллов в подарок при заказе от 1500₽", "test", "tanuki_food_1", true, 1.0),
            Coupon("Zara", "Одежда", "Сезонные скидки до 50%", "test", "zara_clothes_1", true, 1.0),
            Coupon("Карусель", "Продукты", "3 по цене 2 на напитки", "test", "karusel_groceries_1", true, 1.0),
            Coupon("Ламода", "Аксессуары", "Скидка 20% на сумки и рюкзаки", "test", "lamoda_accessories_1", true, 1.0),
            Coupon("Яндекс.Лавка", "Еда", "Промокод на 250₽ для новых пользователей", "test", "lavka_food_1", true, 1.0)
        )
        fun uploadCouponsFromJson(context: Context) {
            val json = context.assets.open("couponsNew.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val coupons = gson.fromJson(json, Array<Coupon>::class.java).toList()
            val db = FirebaseFirestore.getInstance()

            for (coupon in coupons) {
                db.collection("coupons").document(coupon.id).set(coupon)
                    .addOnSuccessListener { Log.d("Firestore", "Uploaded: ${coupon.id}") }
                    .addOnFailureListener { e -> Log.w("Firestore", "Error uploading", e) }
            }
        }

        uploadCouponsFromJson(this)*/
        setContent {
            MainScreen()
        }
    }
}
