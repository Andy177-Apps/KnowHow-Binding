package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.user.UserProfileViewModel
import com.wenbin.knowhowbinding.user.article.UserArticleViewModel
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class UserProfileViewModelFactory(
        private val repository: KnowHowBindingRepository,
        private val userEmail: String
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(repository, userEmail) as T
        }

        if (modelClass.isAssignableFrom(UserArticleViewModel::class.java)) {
            return UserArticleViewModel(repository, userEmail) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}