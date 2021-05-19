package com.wenbin.knowhowbinding.data.source

import androidx.lifecycle.MutableLiveData
import com.wenbin.knowhowbinding.data.*

interface KnowHowBindingRepository {

    suspend fun loginMockData(id: String): Result<User>

    suspend fun createTestedData() : Result<List<Article>>

    suspend fun publish(article: Article): Result<Boolean>

    suspend fun getArticles():  Result<List<Article>>

    suspend fun getLiveChatRooms() : Result<List<ChatRoom>>

    suspend fun addMessage(chatRoom: ChatRoom, message: Message) : Result<Boolean>

    suspend fun postEvent(event: Event): Result<Boolean>
}