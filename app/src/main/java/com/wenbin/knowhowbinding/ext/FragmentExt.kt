package com.wenbin.knowhowbinding.ext

import androidx.fragment.app.Fragment
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Event
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

fun Fragment.getVmFactory(chatRoom: ChatRoom?): MessageViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return MessageViewModelFactory(repository, chatRoom)
}

fun Fragment.getVmFactory(selectedDate: Long): EventViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return EventViewModelFactory(repository, selectedDate)
}