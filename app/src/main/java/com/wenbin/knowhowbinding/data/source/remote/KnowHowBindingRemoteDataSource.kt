package com.wenbin.knowhowbinding.data.source.remote

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Result
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KnowHowBindingRemoteDataSource : KnowHowBindingDataSource {

    private const val PATH_ARTICLES = "articles"
    private const val KEY_CREATED_TIME = "createdTime"

    override suspend fun login(id: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createTestedData(): Result<List<Article>> {
        TODO("not implemented")
    }

    override suspend fun publish(article: Article): Result<Boolean> = suspendCoroutine { continuation ->
        val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
        val document = articles.document()

        article.id = document.id
//        article.createdTime = Calendar.getInstance().timeInMillis

        document
                .set(article)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("wenbin", "Publish: $article")

                        continuation.resume( Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("wenbin","[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
    }

    override suspend fun getArticles(): Result<List<Article>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_ARTICLES)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Article>()
                        for (document in task.result!!) {
                            Log.d("wembin",document.id + " => " + document.data)

                            val article = document.toObject(Article::class.java)
                            list.add(article)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                        Log.w("Wenbin","[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
    }
}



