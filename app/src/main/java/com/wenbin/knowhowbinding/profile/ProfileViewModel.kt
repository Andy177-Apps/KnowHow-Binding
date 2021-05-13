package com.wenbin.knowhowbinding.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.Comment

class ProfileViewModel : ViewModel() {
    private val _comment = MutableLiveData<List<Comment>>()

    val comment: LiveData<List<Comment>>
        get() = _comment

    init {
        createTestedData()
    }

    private fun createTestedData(){
        var defaultData = mutableListOf<Comment>()
        defaultData.run {
            add(Comment("Armin","觀念講解仔細"))
            add(Comment("Mikasa","深入淺出"))

        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _comment.value = defaultData
        Log.d("Wenbin", "_articles.value = $_comment.value")
    }
}