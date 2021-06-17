package com.wenbin.knowhowbinding.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.ext.sortByTimeStamp
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import com.wenbin.knowhowbinding.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*

class CalendarViewModel(
    private val repository: KnowHowBindingRepository
) : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()

    val events: LiveData<List<Event>>
        get() = _events

    var liveEvents = MutableLiveData<List<Event>>()

    // Handle navigation to CreateEventDialogFragment with Selected date by safe arg
    private val _navigationToCreateEventDialogFragment = MutableLiveData<Long>()

    val navigationToCreateEventDialogFragment : LiveData<Long>
        get() = _navigationToCreateEventDialogFragment

    val selectedLiveEvent = MutableLiveData<List<Event>>()

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

        getLiveEventsResult()
        todayDate()
        getEventsResult()
        if (KnowHowBindingApplication.instance.isLiveDataDesign()) {
            getLiveEventsResult()
        }else {
            getEventsResult()
        }
    }

    private fun getLiveEventsResult() {
        liveEvents = repository.getLiveEvents()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }
    private fun getEventsResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllEvents()

            Log.d("wenbin", "EventsResult = $result")
            _events.value = when (result) {
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

    fun createdDailyEvent (toTimeStamp: Long) {
        Log.d("test", "toTimeStamp = $toTimeStamp")

        Log.d("test", "liveEvents.value = ${liveEvents.value}")
        selectedLiveEvent.value = liveEvents.value.sortByTimeStamp(toTimeStamp)
        _navigationToCreateEventDialogFragment.value = toTimeStamp
        Log.d("test", "selectedLiveEvent.value = ${selectedLiveEvent.value}")

    }

    private fun todayDate() {
        _navigationToCreateEventDialogFragment.value = TimeUtil.dateToStamp(LocalDate.now().toString(), Locale.TAIWAN)
    }

}