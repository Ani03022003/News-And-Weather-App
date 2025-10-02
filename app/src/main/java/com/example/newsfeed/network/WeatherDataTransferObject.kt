package com.example.newsfeed.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherData(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    @Json(name = "name")val location: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
): Parcelable

@Parcelize
data class Clouds(
    val all: Int
): Parcelable

@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
): Parcelable

@Parcelize
data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
): Parcelable

@Parcelize
data class Sys(
    val country: String,
    val id: Int = 0,
    val sunrise: Int,
    val sunset: Int,
    val type: Int = 0
): Parcelable

@Parcelize
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
): Parcelable

@Parcelize
data class Wind(
    val deg: Int,
    val speed: Double
): Parcelable