package com.example.newsfeed.repository

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.map
import com.example.newsfeed.database.NewsDatabase
import com.example.newsfeed.database.asDomainModel
import com.example.newsfeed.network.NewsApi
import com.example.newsfeed.network.NewsList
import com.example.newsfeed.network.WeatherApi
import com.example.newsfeed.network.WeatherData
import com.example.newsfeed.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class NewsRepository(private val database: NewsDatabase) {

    val news = database.newsDatabaseDao.getNews().map {
        it.asDomainModel()
    }

    //@RequiresApi(Build.VERSION_CODES.N)
    suspend fun getUpdatedNews(){
        withContext(Dispatchers.IO){
            val date = getDate()
            val updatedNews = NewsApi.retrofitService.getNews(from = date).await()
            println(updatedNews.articles.size)
            database.newsDatabaseDao.insertAll(*updatedNews.asDatabaseModel())
        }
    }

    //@RequiresApi(Build.VERSION_CODES.N)
    private fun getDate(): String {
        val cal = GregorianCalendar()
        cal.add(Calendar.DATE,-1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(cal.time)
    }

}

class WeatherRepository(private val location: Location){
    private val lat = location.latitude
    private val lon = location.longitude
    suspend fun getWeather(): WeatherData {
        return withContext(Dispatchers.IO) {
            WeatherApi.retrofitService.getWeatherDetails(lat = lat, lon = lon)
                .await()
        }
    }
}