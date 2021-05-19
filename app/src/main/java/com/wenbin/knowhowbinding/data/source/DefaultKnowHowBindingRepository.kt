package com.wenbin.knowhowbinding.data.source

import androidx.lifecycle.MutableLiveData
import com.wenbin.knowhowbinding.data.*


class DefaultKnowHowBindingRepository (private val knowHowBindingRemoteDataSource: KnowHowBindingDataSource,
                                       private val knowHowBindingLocalDataSource: KnowHowBindingDataSource,
) : KnowHowBindingRepository {
    override suspend fun loginMockData(id: String): Result<User> {
        return knowHowBindingLocalDataSource.login(id)
    }

    override suspend fun createTestedData(): Result<List<Article>> {
        return knowHowBindingLocalDataSource.createTestedData()
    }

    override suspend fun publish(article: Article): Result<Boolean> {
        return knowHowBindingRemoteDataSource.publish(article)
    }

    override suspend fun getArticles():  Result<List<Article>> {
        return knowHowBindingRemoteDataSource.getArticles()
    }

    override suspend fun getLiveChatRooms(): Result<List<ChatRoom>> {
        return knowHowBindingRemoteDataSource.getLiveChatRooms()
    }

    override suspend fun addMessage(chatRoom: ChatRoom, message: Message): Result<Boolean> {
        return knowHowBindingRemoteDataSource.addMessage(chatRoom, message)
    }

    override suspend fun postEvent(event: Event): Result<Boolean> {
        return knowHowBindingRemoteDataSource.postEvent(event)
    }
}