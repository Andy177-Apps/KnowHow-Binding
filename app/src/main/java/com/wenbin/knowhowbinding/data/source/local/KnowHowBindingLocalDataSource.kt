package com.wenbin.knowhowbinding.data.source.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource


class KnowHowBindingLocalDataSource(val context: Context) : KnowHowBindingDataSource {

    override suspend fun login(id: String): Result<User> {
        return when (id) {
            "wenbin" -> Result.Success((User(
                    id,
                    "AKA小安老師",
                    "wayne@school.appworks.tw"
            )))
            else -> Result.Fail("You have to add $id info in local data source.")
        }
    }

    override suspend fun publish(article: Article): Result<Boolean> {
        TODO("Not yet implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getArticles(): Result<List<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLiveChatRooms(): Result<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMessage(chatRoom: ChatRoom, message: Message): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postEvent(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEvents(): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }


    override suspend fun createTestedData(): Result<List<Article>> {
        var defaultData = mutableListOf<Article>()
        defaultData.run {
            add((Article("leo55576", "交換技能",72345959,"測試用","測試本地端資料",User("Andy117",
            "Wenbin", "leo55576@hotmail.com"))))
             }
        Log.d("DefaultData", "Frist Data = $defaultData")
        return Result.Success(defaultData)
    }




}