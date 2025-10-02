package com.example.newsfeed.newsDisplay

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.adapter.DisplayAdapter
import com.example.newsfeed.database.NewsDatabase
import com.example.newsfeed.domain.News
import com.example.newsfeed.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsListViewModel(
    private val application: Application,
    private val newsAdapter: DisplayAdapter) : AndroidViewModel(application) {

    private val database = NewsDatabase.getInstance(application)
    private val repo = NewsRepository(database)

    private var _dataNull = MutableLiveData<Boolean>()
    val dataNull : LiveData<Boolean>
        get() = _dataNull

    private var _onNewsClicked = MutableLiveData<String?>()
    val onNewsClicked : LiveData<String?>
        get() = _onNewsClicked

    init{
        viewModelScope.launch {
            repo.getUpdatedNews()
        }
    }

    var news :LiveData<List<News>> = repo.news

    fun toastFinished() {
        _dataNull.value = false
    }

    fun onNewsClicked(url: String?) {
        _onNewsClicked.value = url
    }

    fun onNewsClickedFinished() {
        _onNewsClicked.value = null
    }
}