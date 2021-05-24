package com.example.weatherproject.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temp : Int = 23,
    val feelsLike : Int = 32
) : Parcelable

fun getDefaultCity() = City("Дубна", 23.444, 12312.213123)

fun getWorldCities() : List<Weather> {
    return listOf(
            Weather(City("Токио", 51.50803,9.1253),42,44),
            Weather(City("Рим",23.123123,12.4214),23,12),
            Weather(City("Оттава", 46.00751,1.06009),12,-282),
            Weather(City("Мадрид",87.38467,14.977),35,41),
            Weather(City("Париж", 11.412403,19.1233),23,19),
            Weather(City("Киев",8.506743,1.4087564),27,24),
            Weather(City("Пекин", 87.07614,8.610565),42,44),
            Weather(City("Берлин",84.77641,-1.777),23,12),
            Weather(City("Лондон", 99.99143,56.4124),42,44),
            Weather(City("Мехико",32.412423,11.088754),23,12)
    )
}

fun getRussianCities() : List<Weather> {
    return listOf(
            Weather(City("Москва", 55.755826, 37.617299900000035), 1, 2),
            Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3),
            Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6),
            Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7,
                    8),
            Weather(City("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
            Weather(City("Казань", 55.8304307, 49.06608060000008), 11, 12),
            Weather(City("Челябинск", 55.1644419, 61.4368432), 13, 14),
            Weather(City("Омск", 54.9884804, 73.32423610000001), 15, 16),
            Weather(City("Ростов-на-Дону", 47.2357137, 39.701505), 17, 18),
            Weather(City("Уфа", 54.7387621, 55.972055400000045), 19, 20)
    )
}