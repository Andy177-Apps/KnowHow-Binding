package com.wenbin.knowhowbinding.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository

class ArticleViewModel (private val repository: KnowHowBindingRepository) : ViewModel() {
    val  searchEditText = MutableLiveData<String>()
}