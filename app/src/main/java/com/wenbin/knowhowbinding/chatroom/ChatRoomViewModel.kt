package com.wenbin.knowhowbinding.chatroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.data.Words
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wenbin.knowhowbinding.data.Result



class ChatRoomViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {
    private var _updatedChatRooms = MutableLiveData<List<ChatRoom>>()

    val updatedChatRooms: LiveData<List<ChatRoom>>
        get() = _updatedChatRooms

    private val _fakeMessages = MutableLiveData<List<Message>>()

    val fakeMessages: LiveData<List<Message>>
        get() = _fakeMessages
//    private val words = listOf<Words>(Words("請問這週改週三上ge課方便嗎？","2小時前"))

    private val _status = MutableLiveData<LoadApiStatus>()

    val status : LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // The Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    init {
        createTestedData()
        getAllLiveChatRoom()
    }

    private fun getAllLiveChatRoom() {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            val result = repository.getLiveChatRooms()

            _updatedChatRooms.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error-> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            Log.d("wenbin", "_updatedChatRooms.value = ${_updatedChatRooms.value}")
        }
    }
    private fun createTestedData(){
        var defaultData = mutableListOf<Message>()
        defaultData.run {
            add(Message("Levi", listOf(Words("昨天那題有點不懂，求教QQ","4小時前"))))
            add(Message("CC", listOf(Words("如果時薪 700 的話，可嗎？","5小時前"))))
            add(Message("Sasha", listOf(Words("先去吃午餐喔!","7小時前"))))
            add(Message("Ashly ", listOf(Words("今天總圖好像沒位 = =","5/4"))))
        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _fakeMessages.value = defaultData
        Log.d("Wenbin", "_messages.value = $_updatedChatRooms.value")
    }
}