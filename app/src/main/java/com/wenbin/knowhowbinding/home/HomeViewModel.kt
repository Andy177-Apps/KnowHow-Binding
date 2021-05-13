package com.wenbin.knowhowbinding.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    /**
     * For navigate to Post Article Fragment
     */
    private val _navigateToPostArticle = MutableLiveData<Boolean>()

    val navigateToPostArticle: LiveData<Boolean>
        get() = _navigateToPostArticle

    fun navigateToPostArticle() {
        _navigateToPostArticle.value = true
    }

    fun onPostArticleNavigated() {
        _navigateToPostArticle.value = null
    }
}