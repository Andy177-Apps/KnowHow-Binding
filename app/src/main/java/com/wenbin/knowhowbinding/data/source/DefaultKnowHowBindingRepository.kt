package com.wenbin.knowhowbinding.data.source

import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result


class DefaultKnowHowBindingRepository (private val knowHowBindingRemoteDataSource: KnowHowBindingDataSource,
                                       private val knowHowBindingLocalDataSource: KnowHowBindingDataSource,
) : KnowHowBindingRepository {
    override suspend fun createTestedData(): Result<List<Article>> {
        return knowHowBindingLocalDataSource.createTestedData()
    }
}