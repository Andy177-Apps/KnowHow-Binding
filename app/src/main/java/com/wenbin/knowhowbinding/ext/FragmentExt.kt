package com.wenbin.knowhowbinding.ext

import androidx.fragment.app.Fragment
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.factory.AuthorViewModelFactory
import com.wenbin.knowhowbinding.factory.ViewModelFactory

/**
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return ViewModelFactory(repository)
}
//fun Fragment.getVmFactory(author: User?): AuthorViewModelFactory {
//    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
//    return AuthorViewModelFactory(repository, author)
//}