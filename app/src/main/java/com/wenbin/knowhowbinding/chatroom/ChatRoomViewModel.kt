package com.wenbin.knowhowbinding.chatroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.data.Words

class ChatRoomViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()

    val messages: LiveData<List<Message>>
        get() = _messages
    private val words = listOf<Words>(Words("dsf","sdf"))

    init {
        createTestedData()
    }

    private fun createTestedData(){
        var defaultData = mutableListOf<Message>()
        defaultData.run {
            add(Message("AA",words))
            add(Message("BB", listOf(Words("gh","gboi"))))


        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _messages.value = defaultData
        Log.d("Wenbin", "_messages.value = $_messages.value")
    }
}