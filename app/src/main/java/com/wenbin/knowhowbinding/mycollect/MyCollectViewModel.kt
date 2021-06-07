package com.wenbin.knowhowbinding.mycollect

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyCollectViewModel(private val repository: KnowHowBindingRepository)  : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()

    val articles: LiveData<List<Article>>
        get() = _articles

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
//        createTestedData()
        getSavedArticle(UserManager.user.email)
    }

    private fun getSavedArticle(userEmail: String) {
        Log.d("MyCollectFragment", "getUserArticle is used.")

        coroutineScope.launch {

            val result = repository.getSavedArticle(userEmail)
            Log.d("MyCollectFragment", "result = $result")

            _articles.value = when (result) {
                is Result.Success -> {
                    _error.value = null
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
                    _error.value = KnowHowBindingApplication.instance.getString(R.string.you_shall_not_pass)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    private fun createTestedData(){
        var defaultData = mutableListOf<Article>()
        defaultData.run {
//            add(Article(authorName = "Susan Tsai",city = "台北/新北","歡迎技能交換~\n" +
//                    "\n" +
//                    "\n" +
//                    "\uD83C\uDF40簡單介紹花精：\n" +
//                    "\n" +
//                    "巴哈花精是由英國的愛德華．巴哈醫師所建立的系統，一共有38種單方與一種複方急救花精所組成；主要用來平衡情緒，花精的成分天然，無毒無害、無副作用、無依賴性，使用方式簡單，男女老少都適用。\n" +
//                    "\n" +
//                    "\n" +
//                    "\uD83C\uDF1B簡單介紹13月亮曆：\n" +
//                    "\n" +
//                    "如同大家熟悉的星座一樣，13月亮曆也是一個自我認識的好工具，透過出生日期找到自己的Kin，發現自己的特質，一起在時間裡玩耍！\n" +
//                    "\n" +
//                    "\n" +
//                    "若需體驗則採隨喜，10%將作為公益捐贈\n" +
//                    "\n" +
//                    "期待與您相遇！","交換技能"))
//            add(Article("Duncan", "台南市", "前後端開發及手機開發都有幾年經驗\n" +
//                    "對我有興趣的可以找我聊聊", "交換技能"))
        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _articles.value = defaultData
        Log.d("Wenbin", "_articles.value = $_articles.value")
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
}