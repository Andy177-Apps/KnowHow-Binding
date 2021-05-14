package com.wenbin.knowhowbinding.data.source.remote

import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource

object KnowHowBindingRemoteDataSource : KnowHowBindingDataSource {

    override suspend fun createTestedData(): Result<List<Article>> {
        TODO("not implemented")
    }
}