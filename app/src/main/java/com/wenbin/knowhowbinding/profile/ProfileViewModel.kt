package com.wenbin.knowhowbinding.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Comment
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger


class ProfileViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

    private val _comment = MutableLiveData<List<Comment>>()
    val comment: LiveData<List<Comment>>
        get() = _comment

    // Information supposed to demonstrate in UI
    private val _userInfo = MutableLiveData<User>(User())

    val userInfo: LiveData<User>
        get() = _userInfo

    private val _allUsers = MutableLiveData<List<User>>()

    val allUsers: LiveData<List<User>>
        get() = _allUsers

    // Handle navigation to user profile
    private val _navigateToUserProfile = MutableLiveData<User>()

    val navigateToUserProfile: LiveData<User>
        get() = _navigateToUserProfile

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        getUser(UserManager.user.email)

    }

    private fun getUser(userEmail: String) {

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
            _refreshStatus.value = false
        }
    }

    fun getAllUsers() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllUsers()

            _allUsers.value = when (result) {
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_do_not_pass)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    private val _navigateToMyArticle = MutableLiveData<Boolean>()
    val navigateToMyArticle: LiveData<Boolean>
        get() = _navigateToMyArticle

    fun navigateToMyArticle() {
        _navigateToMyArticle.value = true
    }

    fun onMyArticleNavigated() {
        _navigateToMyArticle.value = null
    }

    private val _navigateToMyCollect = MutableLiveData<Boolean>()
    val navigateToMyCollect: LiveData<Boolean>
        get() = _navigateToMyCollect

    fun navigateToMyCollect() {
        _navigateToMyCollect.value = true
    }

    fun onMyCollectNavigated() {
        _navigateToMyCollect.value = null
    }

    private val _navigateToEditProfile = MutableLiveData<Boolean>()
    val navigateToEditProfile: LiveData<Boolean>
        get() = _navigateToEditProfile

    fun navigateToEditProfile() {
        _navigateToEditProfile.value = true
    }

    fun onEditProfileNavigated() {
        _navigateToEditProfile.value = null
    }

    fun navigateToUserProfile(user: User) {
        _navigateToUserProfile.value = user
    }

    fun onUserProfileNavigated() {
        _navigateToUserProfile.value = null
    }

}