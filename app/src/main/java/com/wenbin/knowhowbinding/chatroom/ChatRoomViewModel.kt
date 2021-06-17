package com.wenbin.knowhowbinding.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import com.wenbin.knowhowbinding.login.UserManager


class ChatRoomViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

    private val _filteredChatRooms = MutableLiveData<List<ChatRoom>>()

    val filteredChatRooms: LiveData<List<ChatRoom>>
        get() = _filteredChatRooms

    private var _updatedChatRooms = MutableLiveData<List<ChatRoom>>()

    val updatedChatRooms: LiveData<List<ChatRoom>>
        get() = _updatedChatRooms

    private var _testString = MutableLiveData<List<String>>()

    val testString: LiveData<List<String>>
        get() = _testString

    private val _fakeMessages = MutableLiveData<List<Message>>()

    val fakeMessages: LiveData<List<Message>>
        get() = _fakeMessages

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

    // test function
    fun changer(array: ArrayList<String>) {
        _testString.value = array
    }
}