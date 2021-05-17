package com.wenbin.knowhowbinding.data.source

import androidx.lifecycle.MutableLiveData
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.User

interface KnowHowBindingRepository {

    suspend fun loginMockData(id: String): Result<User>

    suspend fun createTestedData() : Result<List<Article>>

    suspend fun publish(article: Article): Result<Boolean>

    suspend fun getArticles():  Result<List<Article>>

    suspend fun getLiveChatRooms() : Result<List<ChatRoom>>


}