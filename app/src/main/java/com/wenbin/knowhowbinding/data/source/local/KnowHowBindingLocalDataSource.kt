package com.wenbin.knowhowbinding.data.source.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource


class KnowHowBindingLocalDataSource(val context: Context) : KnowHowBindingDataSource {

    override suspend fun login(id: String): Result<User> {
        return when (id) {
            "wenbin" -> Result.Success((User(
                    id,
                    "文彬",
                    "leo55576@gmail.com"
            )))
            "exercise" -> Result.Success((User(
                    id,
                    "柏均",
                    "leo55576@hotmail.com"
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

    override fun getLiveChatRooms(userEmail: String):  MutableLiveData<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean> {
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

    override fun getLiveMessages(emails: List<String>): MutableLiveData<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userEmail: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun getLiveMyEventInvitation(userEmail: String): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptEvent(
        event: Event,
        userEmail: String,
        userName: String,
        userImage: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun declineEvent(event: Event, userEmail: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserFromFollow(userEmail: String, user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserArticle(userEmail: String): Result<List<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedArticle(userEmail: String): Result<List<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun getImageUri(filePath: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun saveArticle(article: Article, userEmail: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowing(userEmail: String): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowedBy(userEmailList: List<String>): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): Result<List<User>> {
        TODO("Not yet implemented")
    }


    override suspend fun createTestedData(): Result<List<Article>> {
        var defaultData = mutableListOf<Article>()
        Log.d("DefaultData", "Frist Data = $defaultData")
        return Result.Success(defaultData)
    }




}