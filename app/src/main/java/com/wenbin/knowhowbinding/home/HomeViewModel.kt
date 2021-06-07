package com.wenbin.knowhowbinding.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.login.UserManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class HomeViewModel(val repository: KnowHowBindingRepository) : ViewModel() {

    val  searchEditText = MutableLiveData<String>()

    private val _articles = MutableLiveData<List<Article>>()

    val articles: LiveData<List<Article>>
        get() = _articles

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    private val _navigateToPostArticle = MutableLiveData<Boolean>()

    val navigateToPostArticle: LiveData<Boolean>
        get() = _navigateToPostArticle

    private var _checked = MutableLiveData<Boolean>()

    val checked: LiveData<Boolean>
        get() = _checked

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
        getArticlesResult()
        getUser(UserManager.user.email)

        Log.d("checkArticles","articles.value in ViewModel = ${articles.value}")
//        practiceMVVMfun()
        // When I am not connected to Google sign mechanism, use the following function to create a simulated user information.
//        autoLogin("wenbin")
    }
    fun checkIfInfoComplete(): Boolean {
        val userInfo = userInfo.value
        return (userInfo?.interestedSubjects.isNullOrEmpty() &&
                userInfo?.talentedSubjects.isNullOrEmpty())
    }

        fun navigateToPostArticle() {
        _navigateToPostArticle.value = true
    }

    fun onPostArticleNavigated() {
        _navigateToPostArticle.value = null
    }

    private fun practiceMVVMfun() {
        coroutineScope.launch {
            repository.createTestedData()
        }
    }

    private fun getArticlesResult() {
        Log.d("checkArticles","fun getArticlesResult is used in ViewModel.")

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getArticles()
            Log.d("wenbin", "ArticlesResult = $result")

            _articles.value = when (result) {
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    private fun autoLogin(id: String) {
        coroutineScope.launch {
            login(id)
        }
    }

    private suspend fun login(id: String): User? = suspendCoroutine {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            val result = repository.loginMockData(id)
            Log.d("wenbin", "result = $result")
            _user.value = when (result) {
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
            Log.d("wenbin", "_user.value =  ${_user.value}")
            assignUserInfo()
            it.resume(_user.value)
        }
    }

    private fun assignUserInfo() {
        val currentUser = _user.value?.let {
            User(
                    id = it.id,
                    email = it.email,
                    name = it.name
            )
        }
        if (currentUser != null) {
            UserManager.user = currentUser
        }
        Log.d("wenbin", "UserManager.user = ${UserManager.user}")
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun saveArticle(article: Article, userEmail: String) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.saveArticle(article, userEmail)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE

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

    fun isChecked(value: Boolean) {
        _checked.value = value
    }
}