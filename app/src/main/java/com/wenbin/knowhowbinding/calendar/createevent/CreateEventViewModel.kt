package com.wenbin.knowhowbinding.calendar.createevent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import com.wenbin.knowhowbinding.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class CreateEventViewModel(
    private val repository: KnowHowBindingRepository,
    private val selectedDate: Long
) : ViewModel()  {

    val title = MutableLiveData<String>()

    val city = MutableLiveData<String>()

    val description = MutableLiveData<String>()

    private val _startTime = MutableLiveData<Long>()

    val startTime : LiveData<Long>
        get() = _startTime

    private val _endTime = MutableLiveData<Long>()

    val endTime : LiveData<Long>
        get() = _endTime

    private val _eventTime = MutableLiveData<Long>()

    val eventTime : LiveData<Long>
        get() = _eventTime

    private val _type = MutableLiveData<String>()

    val type : LiveData<String>
        get() = _type

    val date = TimeUtil.stampToDate(selectedDate)

    private val _isAllDay = MutableLiveData<Boolean>()

    val isAllDay : LiveData<Boolean>
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
        setInitialTime()
    }
    fun getEvent(): Event {
        return Event(
            id = "",
            city = city.value.toString(),
            title = title.value.toString(),
            description = description.value.toString(),

        )
    }

    fun post() {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            Log.d("wenbin", "title.value = ${title.value}")

            val event = Event(id = "leo55576",city = city.value.toString(), title = title.value.toString(), description = description.value.toString())
            Log.d("wenbin", "event = $event")
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun setType(selectedType: String) {
        _type.value = selectedType
    }

    private fun setInitialTime() {
        _eventTime.value = TimeUtil.dateToStamp(date, Locale.TAIWAN)
    }

    fun setAllDay(isAllDay : Boolean) {
        _isAllDay.value = isAllDay
    }
    fun setEventTime(timeStamp: Long) {
        _eventTime.value = timeStamp
    }

    fun setStartIme(timeStamp: Long) {
        _startTime.value = timeStamp
    }

    fun setEndTime(timeStamp: Long) {
        _endTime.value = timeStamp
    }


}