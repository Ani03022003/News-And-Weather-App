package com.example.newsfeed.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDatabaseDao {
    @Query("select * from news_table order by publishedAt desc")
    fun getNews() : LiveData<List<DatabaseNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg news : DatabaseNews)
}