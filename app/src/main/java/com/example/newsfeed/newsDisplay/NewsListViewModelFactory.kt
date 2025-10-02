package com.example.newsfeed.newsDisplay

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.newsfeed.adapter.DisplayAdapter

class NewsListViewModelFactory(private val application: Application,private val newsAdapter: DisplayAdapter) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(NewsListViewModel::class.java))
            return NewsListViewModel(application, newsAdapter) as T
        throw IllegalArgumentException("Class Nor found")
    }
}