package com.wenbin.knowhowbinding.data.source.local

import android.content.Context
import android.util.Log
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.User


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

    override suspend fun createTestedData(): Result<List<Article>> {
        var defaultData = mutableListOf<Article>()
        defaultData.run {
             }
        Log.d("DefaultData", "Frist Data = $defaultData")
        return Result.Success(defaultData)
    }




}