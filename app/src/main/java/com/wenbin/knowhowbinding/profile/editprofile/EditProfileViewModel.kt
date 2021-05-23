package com.wenbin.knowhowbinding.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository

class EditProfileViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

    val identity = MutableLiveData<String>()

    val talentedSubjects = MutableLiveData<String>()

    val interestedSubjects = MutableLiveData<String>()

    val introduction = MutableLiveData<String>()

    /**
     * For navigate to Profile Fragment
     */
    private val _navigateToProfilePage = MutableLiveData<Boolean>()

    val navigateToProfilePage: LiveData<Boolean>
        get() = _navigateToProfilePage

    fun navigateToProfilePage() {
        _navigateToProfilePage.value = true
    }

    fun onProfilePageNavigated() {
        _navigateToProfilePage.value = null
    }
}