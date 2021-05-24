package com.wenbin.knowhowbinding.chatroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.login.UserManager


class ChatRoomViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

//    var LiveChatRooms = MutableLiveData<List<ChatRoom>>()

    private val _filteredChatRooms = MutableLiveData<List<ChatRoom>>()

    val filteredChatRooms: LiveData<List<ChatRoom>>
        get() = _filteredChatRooms

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
        getAllLiveChatRoom(UserManager.user.email)
    }

    private fun getAllLiveChatRoom(userEmail:String) {
        _updatedChatRooms = repository.getLiveChatRooms(userEmail)
        _status.value = LoadApiStatus.DONE
    }

    fun createFilteredChatRooms(filteredChatRoom: List<ChatRoom>) {
        _filteredChatRooms.value = filteredChatRoom
    }
//    private fun getAllLiveChatRoom() {
//        coroutineScope.launch {
//            _status.value = LoadApiStatus.LOADING
//
//            val result = repository.getLiveChatRooms()
//
//            _updatedChatRooms.value = when (result) {
//                is Result.Success -> {
//                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
//                    result.data
//                }
//                is Result.Fail -> {
//                    _error.value = result.error
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                is Result.Error-> {
//                    _error.value = result.exception.toString()
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                else -> {
//                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//            }
//            Log.d("wenbin", "_updatedChatRooms.value = ${_updatedChatRooms.value}")
//        }
//    }
}