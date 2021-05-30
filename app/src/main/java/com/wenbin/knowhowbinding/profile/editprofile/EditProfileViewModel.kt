package com.wenbin.knowhowbinding.profile.editprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {



    var _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    val identity = MutableLiveData<String>()

    val talentedSubjects = MutableLiveData<String>()

    val interestedSubjects = MutableLiveData<String>()

    val introduction = MutableLiveData<String>()

    val itemList: MutableList<String> = ArrayList()

    private var _selectedTags = MutableLiveData<List<String>>()
    val selectedTags : LiveData<List<String>>
        get() = _selectedTags

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

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * For navigate to Profile Fragment
     */
    private val _navigateToProfilePage = MutableLiveData<Boolean>()

    val navigateToProfilePage: LiveData<Boolean>
        get() = _navigateToProfilePage

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")

        getUser(UserManager.user.email)
    }

    fun updateUser(user: User) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            Log.d("EditViewModel", "user = $user")

            when (val result = repository.updateUser(user)) {
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

    fun getUser(): User {
        return User(
                id = UserManager.user.id,
                name = UserManager.user.name,
                email = UserManager.user.email,
                identity = identity.value.toString(),
                talentedSubjects = talentedSubjects.value.toString(),
                interestedSubjects = interestedSubjects.value.toString(),
                introduction = introduction.value.toString()
        )
    }


    fun navigateToProfilePage() {
        _navigateToProfilePage.value = true
    }

    fun onProfilePageNavigated() {
        _navigateToProfilePage.value = null
    }

    private fun leave(isNeedRefresh: Boolean = false) {
        _leave.value = isNeedRefresh
    }

    fun setTags(tag: String, checked: Boolean) {
        if (checked) {
            itemList.remove(tag)
            _selectedTags.value = itemList
        } else {
            itemList.add(tag)
            _selectedTags.value = itemList
        }
    }
}