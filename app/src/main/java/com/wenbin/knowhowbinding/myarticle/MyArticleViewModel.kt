package com.wenbin.knowhowbinding.myarticle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyArticleViewModel(private val repository: KnowHowBindingRepository,
                         userEmail: String) : ViewModel() {

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
//        createTestedData()
        getUserArticle(userEmail)
    }

    private fun createTestedData(){
        var defaultData = mutableListOf<Article>()
        defaultData.run {
//            add(Article(authorName = "Sylvia",city = "Online","目前為一位在職設計師，正準備年底的研究所申請，之前有補過習、上過一些線上課程，不過對於寫作還是有點卡關，主要是論點以及論述句子的問題。\n" +
//                    "\n" +
//                    "以下為對家教的期許及自身狀況：\n" +
//                    "1.對於考試題型以及技巧都已掌握，目前目標為25+\n" +
//                    "2.希望是針對托福的寫作，其他形式的文章不考慮。\n" +
//                    "3.期望每週能批改一至兩篇作文（整合+獨立），希望能在課前完成批改，我會在批改完後自己先檢討，讓上課品質可以更集中在問題討論。\n" +
//                    "\n" +
//                    "費用部分：\n" +
//                    "價格部分可以再商量，由於我的預算有限，但也非常理解「尊重專業」，因此也希望不要來戰費用，希望是真的有意願的人再聊聊。我也能提供一些技能來做交換，UI/UX、吉他、自彈自唱、心理學經驗分享等。 \n" +
//                    "\n" +
//                    "以上，感謝讀到這裡\uD83D\uDE0A","找家教"))
//            add(Article("Ping", "台北", "人在台北\n" +
//                    "時間地點都算彈性\n" +
//                    "\n" +
//                    "希望徵求奇怪興趣/技能的朋友\n" +
//                    "或是可以教我西文、種花草的人\n" +
//                    "\n" +
//                    "我擅長的技能也可點我進去看經歷！", "交換技能"))
        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        _articles.value = defaultData
        Log.d("Wenbin", "_articles.value = $_articles.value")
    }


    private fun getUserArticle(userEmail: String) {
        Log.d("check_userArticles", "getUserArticle is used.")

        coroutineScope.launch {

            val result = repository.getUserArticle(userEmail)
            Log.d("check_userArticles", "result = $result")

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
}