package com.wenbin.knowhowbinding.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository

class FollowingViewModel (private val repository: KnowHowBindingRepository) : ViewModel() {

    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    // Handle navigation to user profile
    private val _navigateToUserProfile = MutableLiveData<User>()

    val navigateToUserProfile: LiveData<User>
        get() = _navigateToUserProfile

    init {

    }

    fun navigateToUserProfile(user: User) {
        _navigateToUserProfile.value = user
    }

    fun onUserProfileNavigated() {
        _navigateToUserProfile.value = null
    }
}