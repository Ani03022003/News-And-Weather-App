package com.example.newsfeed.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsfeed.domain.News
import com.example.newsfeed.network.Source
import kotlinx.parcelize.Parcelize

@Entity(tableName = "news_table")
data class DatabaseNews(
    val name: String?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    //val source: Source,
    val title: String?,
    @PrimaryKey
    val url: String = "Unknown",
    val urlToImage: String?
)

fun List<DatabaseNews>.asDomainModel() : List<News> {
    return map{
        News(
            name = it.name,
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}