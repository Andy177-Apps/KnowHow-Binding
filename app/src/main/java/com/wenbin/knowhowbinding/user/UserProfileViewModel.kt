package com.wenbin.knowhowbinding.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserProfileViewModel(private val repository: KnowHowBindingRepository,
userEmail: String):ViewModel() {
    // Use arguments to receive data, and then send it to the select User Email here
    val selectedUserEmail = userEmail


    private val _userInfo = MutableLiveData<User>(User(
    ))

    val userInfo: LiveData<User>
        get() = _userInfo

    private val _myInfo = MutableLiveData<User>()

    val myInfo: LiveData<User>
        get() = _myInfo

    private val _userArticles = MutableLiveData<List<Article>>()

    val userArticles: LiveData<List<Article>>
        get() = _userArticles

    // Handle navigation to order detail
    private val _navigateToMyArticle = MutableLiveData<String>()

    val navigateToMyArticle: LiveData<String>
        get() = _navigateToMyArticle

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        _status.value = LoadApiStatus.LOADING
        getUser(selectedUserEmail)
        getUserArticle(selectedUserEmail)
    }

    private var doneProgressCount = 3
    private fun doneProgress() {

        doneProgressCount--
        if (doneProgressCount == 0) _status.value = LoadApiStatus.DONE
    }

    fun createChatRoom(): ChatRoom {

        val attendeeOne = UserInfo().apply{
            userEmail = UserManager.user.email
            userName = UserManager.user.name
            userImage = UserManager.user.image
        }
        val attendeeTwo = UserInfo().apply{
            userEmail = selectedUserEmail
            userInfo.value?.let {
                userName = it.name
                userImage = it.image
            }
        }

        return ChatRoom(
        attendeesInfo = listOf(attendeeOne, attendeeTwo),
        attendees  = listOf(UserManager.user.email, selectedUserEmail),
        attenderName = listOf(UserManager.user.name, userInfo.value!!.name)
        )
    }

    fun postChatRoom(chatRoom: ChatRoom) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postChatRoom(chatRoom)) {
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.connect_fails)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun getUser(userEmail: String) {
        Logger.d("Check_follow, getUser is used.")

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getUser(userEmail)

            _userInfo.value = when (result) {
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
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.connect_fails)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    fun getMyUserInfo(userEmail: String) {
        coroutineScope.launch {

            val result = repository.getUser(userEmail)

            _myInfo.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    doneProgress()
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_do_not_pass)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    fun postUserToFollow(userEmail: String, user: User) {

        coroutineScope.launch {

            when (val result = repository.postUserToFollow(userEmail, user)) {
                is Result.Success -> {
                    _error.value = null
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_do_not_pass)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }

    }

    fun removeUserFromFollow(userEmail: String, user: User) {

        coroutineScope.launch {

            when (val result = repository.removeUserFromFollow(userEmail, user)) {
                is Result.Success -> {
                    _error.value = null
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_do_not_pass)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }

    }

    private fun getUserArticle(userEmail: String) {

        coroutineScope.launch {

            val result = repository.getUserArticle(userEmail)
            Logger.d("Check_userArticles, result = ${result}")

            _userArticles.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    doneProgress()
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_do_not_pass)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    fun navigateToMyArticle(userEmail: String) {
        _navigateToMyArticle.value = userEmail
    }

    fun onMyArticleNavigated() {
        _navigateToMyArticle.value = null
    }

}