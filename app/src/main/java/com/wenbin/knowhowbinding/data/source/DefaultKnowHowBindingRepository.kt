package com.wenbin.knowhowbinding.data.source

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.remote.KnowHowBindingRemoteDataSource


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

    override fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>> {
        return knowHowBindingRemoteDataSource.getLiveChatRooms(userEmail)
    }

    override suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean> {
        return knowHowBindingRemoteDataSource.postMessage(emails, message)
    }

    override suspend fun postEvent(event: Event): Result<Boolean> {
        return knowHowBindingRemoteDataSource.postEvent(event)
    }

    override suspend fun getAllEvents(): Result<List<Event>> {
        return knowHowBindingRemoteDataSource.getAllEvents()
    }

    override fun getLiveEvents(userEmail: String): MutableLiveData<List<Event>> {
        return knowHowBindingRemoteDataSource.getLiveEvents(userEmail)
    }

    override fun getLiveMessages(emails: List<String>): MutableLiveData<List<Message>> {
        return KnowHowBindingRemoteDataSource.getLiveMessages(emails)
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.updateUser(user)
    }

    override suspend fun getUser(userEmail: String): Result<User> {
        return KnowHowBindingRemoteDataSource.getUser(userEmail)
    }

    override fun getLiveMyEventInvitation(userEmail: String): MutableLiveData<List<Event>> {
        return KnowHowBindingRemoteDataSource.getLiveMyEventInvitation(userEmail)
    }

    override suspend fun acceptEvent(
        event: Event,
        userEmail: String,
        userName: String,
        userImage: String
    ): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.acceptEvent(event, userEmail, userName, userImage)
    }

    override suspend fun declineEvent(event: Event, userEmail: String): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.declineEvent(event, userEmail)
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.postChatRoom(chatRoom)
    }

    override suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.postUserToFollow(userEmail, user)
    }

    override suspend fun removeUserFromFollow(userEmail: String, user: User): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.removeUserFromFollow(userEmail, user)
    }

    override suspend fun getUserArticle(userEmail: String): Result<List<Article>> {
        return KnowHowBindingRemoteDataSource.getUserArticle(userEmail)
    }

    override suspend fun getSavedArticle(userEmail: String): Result<List<Article>> {
        return KnowHowBindingRemoteDataSource.getSavedArticle(userEmail)
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.postUser(user)
    }

    override suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser> {
        return KnowHowBindingRemoteDataSource.firebaseAuthWithGoogle(idToken)
    }

    override suspend fun getImageUri(filePath: String): Result<String> {
        return KnowHowBindingRemoteDataSource.getImageUri(filePath)
    }

    override suspend fun saveArticle(article: Article, userEmail: String): Result<Boolean> {
        return KnowHowBindingRemoteDataSource.saveArticle(article, userEmail)
    }

    override suspend fun getFollowing(userEmail: String): Result<List<User>> {
        return KnowHowBindingRemoteDataSource.getFollowing(userEmail)
    }

    override suspend fun getFollowedBy(userEmailList: List<String>): Result<List<User>> {
        return KnowHowBindingRemoteDataSource.getFollowedBy(userEmailList)
    }

    override suspend fun getAllUsers(): Result<List<User>> {
        return KnowHowBindingRemoteDataSource.getAllUsers()
    }
}