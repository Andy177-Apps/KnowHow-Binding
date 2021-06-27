package com.wenbin.knowhowbinding.search.searchresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.ext.sortByUserAnswer
import com.wenbin.knowhowbinding.util.Logger

class SearchResultViewModel(private val repository: KnowHowBindingRepository, private val arguments: Answer) : ViewModel() {

    private val userAnswer = arguments

    private val _allUsers = MutableLiveData<List<User>>()

    val allUsers: LiveData<List<User>>
        get() = _allUsers

    // list of users after filtering
    private val _usersWithMatch = MutableLiveData<List<User>>()

    val usersWithMatch: LiveData<List<User>>
        get() = _usersWithMatch

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
        getAllUsers()
        Logger.d("checkSearchList, userAnswer in viewModel = $userAnswer")
    }

    private fun getAllUsers() {

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

    fun navigateToUserProfile(user: User) {
        _navigateToUserProfile.value = user
    }

    fun onUserProfileNavigated() {
        _navigateToUserProfile.value = null
    }

    fun createSortedList(users: List<User>) {
        Logger.d("checkSearchList, allUsers in viewModel = $users")
        _usersWithMatch.value = users.sortByUserAnswer(userAnswer)
    }
}