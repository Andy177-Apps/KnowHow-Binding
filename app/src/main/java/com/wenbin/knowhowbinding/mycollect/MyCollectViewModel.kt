package com.wenbin.knowhowbinding.mycollect

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.Article

class MyCollectViewModel  : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()

    val articles: LiveData<List<Article>>
        get() = _articles

    init {
//        createTestedData()
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
}