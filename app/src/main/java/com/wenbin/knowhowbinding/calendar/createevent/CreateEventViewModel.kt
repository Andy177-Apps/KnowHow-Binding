package com.wenbin.knowhowbinding.calendar.createevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import com.wenbin.knowhowbinding.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CreateEventViewModel(
        private val repository: KnowHowBindingRepository,
        selectedDate: Long
) : ViewModel() {

    val title = MutableLiveData<String>()

    val city = MutableLiveData<String>()

    val description = MutableLiveData<String>()

    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    private var _followingName = MutableLiveData<ArrayList<String>>()

    val followingName: LiveData<ArrayList<String>>
        get() = _followingName

    private val _startTime = MutableLiveData<Long>()

    val startTime: LiveData<Long>
        get() = _startTime

    private val _endTime = MutableLiveData<Long>()

    val endTime: LiveData<Long>
        get() = _endTime

    private val _eventTime = MutableLiveData<Long>()

    val eventTime: LiveData<Long>
        get() = _eventTime

    private val _type = MutableLiveData<String>()

    val type: LiveData<String>
        get() = _type

    private val _invitation = MutableLiveData<String>()

    val invitation: LiveData<String>
        get() = _invitation

    private val _multipleInvitation = MutableLiveData<List<String>>()

    val multipleInvitation: LiveData<List<String>>
        get() = _multipleInvitation

    val date = TimeUtil.stampToDate(selectedDate)

    private val _isAllDay = MutableLiveData<Boolean>()

    private val isAllDay: LiveData<Boolean>
        get() = _isAllDay

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

    //Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    //the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        getUser(UserManager.user.email)
        setInitialTime()
    }

    // Get the user information in the first place.
    private fun getUser(userEmail: String) {
        Logger.d("getUser is used")
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
                    _error.value =
                            KnowHowBindingApplication.instance.getString(R.string.connect_fails)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    // Set content from xml to Event data
    fun getEvent(): Event {
        //Where, id and createdTime are both assigned in fun postEvent at RemoteDataSource
        return Event(
                city = city.value.toString(),
                title = title.value.toString(),
                description = description.value.toString(),
                creatorName = "Wenbin",
                creatorImage = UserManager.user.image,
                attendees = listOf(UserManager.user.email),
                attendeesName = listOf(UserManager.user.name),
                tag = type.value.toString(),
                eventTime = eventTime.value ?: -1,
                startTime = if (isAllDay.value == false) startTime.value ?: -1L else -1L,
                endTime = if (isAllDay.value == false) endTime.value ?: -1L else -1L,
                invitation = _multipleInvitation.value ?: listOf(),
                attendeesImage = listOf(userInfo.value!!.image)
        )
    }

    fun isFormFilled(): Boolean {
        return city.value !== null &&
                title.value !== null &&
                type.value !== 0.toString()
    }

    fun post(event: Event) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            Logger.d("title.value = ${title.value}")

            Logger.d("event = $event")
            when (val result = repository.postEvent(event)) {
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
                    _error.value =
                            KnowHowBindingApplication.instance.getString(R.string.connect_fails)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun setType(selectedType: String) {
        _type.value = selectedType
    }

    fun setInvitation(selectedFollowing: Int) {
        if (selectedFollowing != 0) {
            _invitation.value = userInfo.value?.following?.get(selectedFollowing - 1)?.userEmail
        }
    }

    fun setMultipleInvitation(selectedFollowing: List<String>) {
        Logger.d("setMultipleInvitation is used.")
        Logger.d("selectedFollowing = $selectedFollowing")

        var listNumber = mutableListOf<Int>()

        for ((i, item) in _userInfo.value!!.followingName.withIndex()) {
            if (selectedFollowing.contains(item)) {
                println(i)
                println(item)
                listNumber.add(i)
            }
        }

        Logger.d("Final listNumber = $listNumber")

        var listEmail = mutableListOf<String>()

        for (item in listNumber) {
            listEmail.add(_userInfo.value!!.following[item].userEmail)
        }
        Logger.d("Final listEmail = $listEmail")
        _multipleInvitation.value = listEmail
    }

    private fun setInitialTime() {
        _eventTime.value = TimeUtil.dateToStamp(date, Locale.TAIWAN)
    }

    fun setAllDay(isAllDay: Boolean) {
        _isAllDay.value = isAllDay
    }

    fun setStartIme(timeStamp: Long) {
        _startTime.value = timeStamp
    }

    fun setEndTime(timeStamp: Long) {
        _endTime.value = timeStamp
    }

    fun getFollowingName(array: ArrayList<String>) {
        _followingName.value = array
    }
}