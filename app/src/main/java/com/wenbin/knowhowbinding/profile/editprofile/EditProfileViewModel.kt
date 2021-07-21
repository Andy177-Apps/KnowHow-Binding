package com.wenbin.knowhowbinding.profile.editprofile

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidbuts.multispinnerfilter.*
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

    private var _userInfo = MutableLiveData<User>( User() )

    val userInfo: LiveData<User>
        get() = _userInfo

    val identity = MutableLiveData<String>()

    private var isBgImageUpdated = false

    val introduction = MutableLiveData<String>()
    val talentedList: MutableList<String> = ArrayList()
    private val interestedList: MutableList<String> = ArrayList()

    private var imageUrlPath : String = ""
    private var bgImageUrlPath : String = ""

    //Consequence for selected chip talentedSubjects
    private var _selectedTalented = MutableLiveData<List<String>>()

    private val selectedTalented : LiveData<List<String>>
        get() = _selectedTalented

    //Consequence for selected chip interestedSubjects
    private var _selectedInterested = MutableLiveData<List<String>>()

    private val selectedInterested : LiveData<List<String>>
        get() = _selectedInterested

    //Variables for editable component
    private var _selectedGender = MutableLiveData<String>()

    private val selectedGender : LiveData<String>
        get() = _selectedGender

    //Consequence for selected city
    private var _selectedCity = MutableLiveData<String>()

    private val selectedCity : LiveData<String>
        get() = _selectedCity

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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.connect_fails)
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.connect_fails)
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
                introduction = introduction.value?.toString() ?: userInfo.value!!.introduction,
                city = selectedCity.value?.toString() ?: userInfo.value!!.city,
                gender = selectedGender.value?.toString() ?: userInfo.value!!.gender,
                identity = identity.value?.toString() ?: userInfo.value!!.identity,
                talentedSubjects = selectedTalented.value ?: userInfo.value!!.talentedSubjects,
                interestedSubjects = selectedInterested.value ?: userInfo.value!!.interestedSubjects,
                image = if (imageUrlPath != "") imageUrlPath else userInfo.value!!.image,
                bgImage = if (bgImageUrlPath != "") {
                    bgImageUrlPath
                } else {
                    userInfo.value!!.bgImage
                }
        )
    }

    fun getImageUri(filePath: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getImageUri(filePath)

            imageUrlPath = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    ""
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    ""
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.connect_fails)
                    _status.value = LoadApiStatus.ERROR
                    ""
                }
            }
            _refreshStatus.value = false
        }
    }

    fun getBgImageUri(filePath: String) {

        isBgImageUpdated = true

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getImageUri(filePath)

            bgImageUrlPath = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    ""
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    ""
                }
                else -> {
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.connect_fails)
                    _status.value = LoadApiStatus.ERROR
                    ""
                }
            }
            _refreshStatus.value = false
        }
    }

    fun navigateToProfilePage() {
        if (isBgImageUpdated) {
            if (bgImageUrlPath != "") {
                _navigateToProfilePage.value = true
            } else {
                Toast.makeText(KnowHowBindingApplication.appContext, "照片上傳中請稍後再儲存", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            _navigateToProfilePage.value = true
        }
    }

    fun onProfilePageNavigated() {
        _navigateToProfilePage.value = null
    }

    private fun leave(isNeedRefresh: Boolean = false) {
        _leave.value = isNeedRefresh
    }

    fun setTalented(tag: String, checked: Boolean) {
        if (checked) {
            talentedList.remove(tag)
            _selectedTalented.value = talentedList
        } else {
            talentedList.add(tag)
            _selectedTalented.value = talentedList
        }
    }

    private fun setTalented(list: List<String>) {
        _selectedTalented.value = list
    }

    fun setInterested(tag: String, checked: Boolean) {
        if (checked) {
            interestedList.remove(tag)
            _selectedInterested.value = interestedList
        } else {
            interestedList.add(tag)
            _selectedInterested.value = interestedList
        }
    }

    private fun setInterested(list: List<String>) {
        _selectedInterested.value = list
    }

    fun setGender(gender: String) {
        _selectedGender.value = gender
    }

    fun setCity(selectedType: String) {
        _selectedCity.value = selectedType
    }

    fun changeStringToKeyPairBoolData(listType: Array<out String>): MutableList<KeyPairBoolData> {
        val listArrayType: MutableList<KeyPairBoolData> = ArrayList()
        for (i in listType.indices) {
            val h = KeyPairBoolData()
            h.id = (i + 1).toLong()
            h.name = listType[i]
            h.isSelected = false
            listArrayType.add(h)
        }
        return listArrayType
    }

    fun setMultipleSpinner(multipleItemSelectionSpinner: MultiSpinnerSearch, isSearchEnabled: Boolean, setSearchHint: String, setClearText: String, setEmptyTitle: String, listArray: MutableList<KeyPairBoolData>, listType: String) {
        multipleItemSelectionSpinner.isSearchEnabled = isSearchEnabled
        multipleItemSelectionSpinner.setSearchHint(setSearchHint)
        multipleItemSelectionSpinner.setClearText(setClearText)
        multipleItemSelectionSpinner.setEmptyTitle(setEmptyTitle)
        multipleItemSelectionSpinner.setItems(
                listArray,
                MultiSpinnerListener { items ->
                    val list = mutableListOf<String>()

                    for (i in items.indices) {
                        if (items[i].isSelected) {
                            Logger.d(i.toString() + " : " + items[i].name + " : " + items[i].isSelected)
                            list.add(items[i].name)
                        }
                    }

                    Logger.d("CheckSelected, Final $listType list =$list")

                    when (listType) {
                        "talented" -> setTalented(list)
                        "interested" -> setInterested(list)
                    }
                })
    }

    fun setSingleSpinner(singleItemSelectionSpinner: SingleSpinnerSearch, isSearchEnabled: Boolean, setSearchHint: String, listArrayType: MutableList<KeyPairBoolData>) {
        // Pass true If you want searchView above the list. Otherwise false. default = true.
        singleItemSelectionSpinner.isSearchEnabled = isSearchEnabled

        // A text that will display in search hint.
        singleItemSelectionSpinner.setSearchHint(setSearchHint)
        singleItemSelectionSpinner.setItems(
                listArrayType,
                object : SingleSpinnerListener {
                    override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                        Logger.d("CheckSelected, Item : " + selectedItem.name)
                        setCity(selectedItem.name)
                    }

                    override fun onClear() {
                        Toast.makeText(
                                KnowHowBindingApplication.appContext,
                                "Cleared Selected Item",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                })
    }
}