package com.wenbin.knowhowbinding.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

    private val _selectedType = MutableLiveData<String>()

    val selectedType: LiveData<String>
        get() = _selectedType

    private val _selectedCity = MutableLiveData<List<String>>()

    val selectedCity: LiveData<List<String>>
        get() = _selectedCity

    private val _selectedGender = MutableLiveData<String>()

    val selectedGender: LiveData<String>
        get() = _selectedGender

    private val _selectedCategory = MutableLiveData<String>()

    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    private val _selectedSubject = MutableLiveData<List<String>>()

    val selectedSubject: LiveData<List<String>>
        get() = _selectedSubject


    private val _articles = MutableLiveData<List<Article>>()

    val articles: LiveData<List<Article>>
        get() = _articles

    private var _listSubject = MutableLiveData<Array<String>>()

    val listSubject: LiveData<Array<String>>
        get() = _listSubject

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
    }

    private fun getArticlesResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getArticles()

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
                    _error.value =
                        KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun selectSubjects(selectedItem: String) {
        Log.d("MultipleSpinner", "selectedItem = $selectedItem")

        _listSubject.value = KnowHowBindingApplication.instance.resources.getStringArray(R.array.default_array)
        val array: Array<String> = when (selectedItem) {
            "Language" -> KnowHowBindingApplication.instance.resources.getStringArray(R.array.language_array)
            "Curriculum" -> KnowHowBindingApplication.instance.resources.getStringArray(R.array.curriculum_array)
            "Music" -> KnowHowBindingApplication.instance.resources.getStringArray(R.array.music_array)
            "Art" -> KnowHowBindingApplication.instance.resources.getStringArray(R.array.art_array)
            "Sport" -> KnowHowBindingApplication.instance.resources.getStringArray(R.array.sport_array)
            else -> KnowHowBindingApplication.instance.resources.getStringArray(R.array.exam_array)
        }
        Log.d("MultipleSpinner", "array = $array")

        _listSubject.value = array

    }

    fun setupType(type: String) {
        _selectedType.value = type
    }

    fun setupCity(city: List<String>) {
        _selectedCity.value = city
    }

    fun setupGender(gender: String) {
        _selectedGender.value = gender
    }

    fun setupCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setupSubject(subject: List<String>) {
        _selectedSubject.value = subject
    }

}