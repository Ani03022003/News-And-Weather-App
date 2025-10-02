package com.example.newsfeed.network

import android.os.Parcelable
import com.example.newsfeed.database.DatabaseNews
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class NewsList(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

@Parcelize
data class Article(
    @Json(name = "author") val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source,
    val title: String?,
    val url: String = "Unknown",
    val urlToImage: String?
) : Parcelable

@Parcelize
data class Source(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
) : Parcelable



fun NewsList.asDatabaseModel() : Array<DatabaseNews> {
    return articles.map{
        DatabaseNews(
            name = it.source.name,
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage)
    }.toTypedArray()
}