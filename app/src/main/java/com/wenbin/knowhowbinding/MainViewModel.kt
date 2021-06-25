package com.wenbin.knowhowbinding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.CurrentFragmentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.login.UserManager

class MainViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    // Save button in edit page is pressed.
    val saveIsPressed = MutableLiveData<Boolean>()

    // isAsked user to edit information
    // The reason for putting variables in Main ViewModel instead of Home ViewModel is to notify the user only once.
    val noticed = MutableLiveData<Boolean>(false)

    private val _userInfo = MutableLiveData<User>(User())

    val userInfo: LiveData<User>
        get() = _userInfo

    private val _userArticles = MutableLiveData<List<Article>>()

    val userArticles: LiveData<List<Article>>
        get() = _userArticles

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
        getUser(UserManager.user.email)
        getUserArticle(UserManager.user.email)
    }

    /**
     * At the beginning, take down all the information of
     * the App user that you need, such as personal articles or
     * personal information, and then ask for MainViewModel if necessary,
     * so that you don’t have to rewrite every leaf by yourself.
     * And because the data has been loaded long ago, the page loading
     * time may be relatively short
     */

    fun getUser(userEmail: String) {

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

    fun getUserArticle(userEmail: String) {
        Log.d("MyArticleFragment", "getUserArticle is used.")

        coroutineScope.launch {

            val result = repository.getUserArticle(userEmail)
            Log.d("MyArticleFragment", "result = $result")

            _userArticles.value = when (result) {
                is Result.Success -> {
                    _error.value = null
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

    fun setUpUser(user: User) {
        _userInfo.value = user
    }
}