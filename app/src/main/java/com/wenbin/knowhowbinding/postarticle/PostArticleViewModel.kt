package com.wenbin.knowhowbinding.postarticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
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
import java.util.*

class PostArticleViewModel(
        private val repository: KnowHowBindingRepository
) : ViewModel() {
    var title: String? = null
    var category: String? = null
    var content: String? = null

    private val _userInfo = MutableLiveData<User>()

    val userInfo: LiveData<User>
        get() = _userInfo

    private val _article = MutableLiveData<Article>()

    var articleType = MutableLiveData<String>()
    var articleCity = MutableLiveData<String>()
    var articleFind = MutableLiveData<String>()
    var articleGive = MutableLiveData<String>()
    var articleContent = MutableLiveData<String>()


    val article: LiveData<Article>
        get() = _article

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    //Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    //the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUser(UserManager.user.email)
    }

    fun getArticle(): Article {
        Logger.d("check_article, UserManager.user.image = ${UserManager.user.image}")
        return Article(

                id = "",
                createdTime = 0,
                author = User(
                        id = _userInfo.value!!.id,
                        name = _userInfo.value!!.name,
                        email = UserManager.user.email,
                        image = _userInfo.value!!.image,
                        identity = _userInfo.value!!.identity),
                type = articleType.value.toString(),
                city = articleCity.value.toString(),
                find = articleFind.value.toString(),
                give = articleGive.value.toString(),
                content = articleContent.value.toString()
        )
    }

    fun publish(article: Article) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.publish(article)) {
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

    fun addData() {
        var newTitle = title
        var newCategory = category
        var newContent = content
        val articles = FirebaseFirestore.getInstance()
                .collection("articles")
        val document = articles.document()
        val data = hashMapOf(
                "author" to hashMapOf(
                        "email" to "wayne@school.appworks.tw",
                        "id" to "waynechen323",
                        "name" to "AKA小安老師"
                ),
                "title" to newTitle,
                "content" to newContent,
                "createdTime" to Calendar.getInstance().timeInMillis,
                "id" to document.id,
                "category" to newCategory
        )
        document.set(data)
    }

    var db = FirebaseFirestore.getInstance()

    fun getData() {
        db.collection("Friends")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Logger.d("${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Logger.d("Error getting documents: $exception")
                }

    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
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
        }
    }
}