package com.wenbin.knowhowbinding.profile

import android.util.Log
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
    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

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
//        createTestedData()
        getUser(UserManager.user.email)
    }

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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    private fun createTestedData(){
        var defaultData = mutableListOf<Comment>()
        defaultData.run {
            add(Comment("Armin","觀念講解仔細"))
            add(Comment("Mikasa","深入淺出"))

        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _comment.value = defaultData
        Log.d("Wenbin", "_articles.value = $_comment.value")
    }

    /**
     * For navigate to My Article Fragment
     */
    private val _navigateToMyArticle = MutableLiveData<Boolean>()

    val navigateToMyArticle: LiveData<Boolean>
        get() = _navigateToMyArticle

    fun navigateToMyArticle() {
        _navigateToMyArticle.value = true
    }

    fun onMyArticleNavigated() {
        _navigateToMyArticle.value = null
    }

    /**
     * For navigate to My Collect Fragment
     */
    private val _navigateToMyCollect = MutableLiveData<Boolean>()

    val navigateToMyCollect: LiveData<Boolean>
        get() = _navigateToMyCollect

    fun navigateToMyCollect() {
        _navigateToMyCollect.value = true
    }

    fun onMyCollectNavigated() {
        _navigateToMyCollect.value = null
    }

    /**
     * For navigate to Edit Profile Fragment
     */
    private val _navigateToEditProfile = MutableLiveData<Boolean>()

    val navigateToEditProfile: LiveData<Boolean>
        get() = _navigateToEditProfile

    fun navigateToEditProfile() {
        _navigateToEditProfile.value = true
    }

    fun onEditProfileNavigated() {
        _navigateToEditProfile.value = null
    }

}