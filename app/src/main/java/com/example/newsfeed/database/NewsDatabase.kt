package com.example.newsfeed.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseNews::class], version = 7, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val newsDatabaseDao : NewsDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE : NewsDatabase? = null

        fun getInstance(context: Context) : NewsDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        "news_table"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}