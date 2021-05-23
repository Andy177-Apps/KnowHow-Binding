package com.wenbin.knowhowbinding.chatroom.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MessageViewModel(
        private val repository: KnowHowBindingRepository,
        userEmail : String,
        userName : String
) : ViewModel() {

    val currentChattingUser = userEmail

    val currentChattingName = userName

    private val _identified = MutableLiveData<ChatRoom>()

    val identified : LiveData<ChatRoom>
        get() = _identified

    val textSend = MutableLiveData<String>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }


    private fun postMessage(userEmails: List<String>, message: Message) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postMessage(userEmails, message)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun getUserEmails(userOneEmail: String, userTwoEmail: String): List<String> {
        return listOf(userOneEmail,userTwoEmail)
    }

    fun getMessage(): Message {
        return Message(
                id = "",
                senderName = UserManager.user.name,
                senderImage = UserManager.user.image,
                senderEmail = UserManager.user.email,
                text = textSend.value.toString(),
                createdTime = 0L
        )
    }
    fun sendMessage(myEmail: String, friendEmail: String) {
        postMessage(getUserEmails(myEmail,friendEmail), getMessage())
    }
    private fun leave(isNeedRefresh: Boolean = false) {
        _leave.value = isNeedRefresh
    }
}