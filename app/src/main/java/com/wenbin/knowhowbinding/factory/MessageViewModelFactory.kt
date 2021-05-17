package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.chatroom.message.MessageViewModel
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MessageViewModelFactory(
        private val repository: KnowHowBindingRepository,
        private val chatRoom : ChatRoom?
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return MessageViewModel(repository, chatRoom) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}