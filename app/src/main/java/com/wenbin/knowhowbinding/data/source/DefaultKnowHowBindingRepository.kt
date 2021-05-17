package com.wenbin.knowhowbinding.data.source

import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.User


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
}