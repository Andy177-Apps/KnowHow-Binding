package com.wenbin.knowhowbinding.data.source

import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result

interface KnowHowBindingDataSource {

    suspend fun createTestedData() : Result<List<Article>>

}