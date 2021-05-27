package com.wenbin.knowhowbinding.ext

import androidx.fragment.app.Fragment
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.factory.CreateEventViewModelFactory
import com.wenbin.knowhowbinding.factory.EventViewModelFactory
import com.wenbin.knowhowbinding.factory.MessageViewModelFactory
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

fun Fragment.getVmFactory(userEmail: String, userName: String): MessageViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return MessageViewModelFactory(repository, userEmail, userName)
}

fun Fragment.getVmFactory(selectedDate: Long): CreateEventViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return CreateEventViewModelFactory(repository, selectedDate)
}

fun Fragment.getVmFactory(event: Event) : EventViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return EventViewModelFactory(repository, event)
}