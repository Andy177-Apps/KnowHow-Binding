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

    /**
     * For navigate to My Article Fragment
     */
    private val _navigateToMyArticle = MutableLiveData<Boolean>()

    val navigateToMyArticle: LiveData<Boolean>
        get() = _navigateToMyArticle

    fun navigateToMyArticle() {
        _navigateToMyArticle.value = true
    }

    fun onMyArticleNavigated() {
        _navigateToMyArticle.value = null
    }

    /**
     * For navigate to My Collect Fragment
     */
    private val _navigateToMyCollect = MutableLiveData<Boolean>()

    val navigateToMyCollect: LiveData<Boolean>
        get() = _navigateToMyCollect

    fun navigateToMyCollect() {
        _navigateToMyCollect.value = true
    }

    fun onMyCollectNavigated() {
        _navigateToMyCollect.value = null
    }

    /**
     * For navigate to Edit Profile Fragment
     */
    private val _navigateToEditProfile = MutableLiveData<Boolean>()

    val navigateToEditProfile: LiveData<Boolean>
        get() = _navigateToEditProfile

    fun navigateToEditProfile() {
        _navigateToEditProfile.value = true
    }

    fun onEditProfileNavigated() {
        _navigateToEditProfile.value = null
    }

}