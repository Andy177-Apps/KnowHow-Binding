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
    private val words = listOf<Words>(Words("請問這週改週三上課方便嗎？","2小時前"))

    init {
        createTestedData()
    }

    private fun createTestedData(){
        var defaultData = mutableListOf<Message>()
        defaultData.run {
            add(Message("Gabi",words))
            add(Message("Levi", listOf(Words("昨天那題有點不懂，求教QQ","4小時前"))))
            add(Message("CC", listOf(Words("如果時薪 700 的話，可嗎？","5小時前"))))
            add(Message("Sasha", listOf(Words("先去吃午餐喔!","7小時前"))))
            add(Message("Ashly ", listOf(Words("今天總圖好像沒位 = =","5/4"))))


        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _messages.value = defaultData
        Log.d("Wenbin", "_messages.value = $_messages.value")
    }
}