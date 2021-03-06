package com.wenbin.knowhowbinding.data.source

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.wenbin.knowhowbinding.data.*

interface KnowHowBindingDataSource {

    suspend fun login(id: String): Result<User>

    suspend fun createTestedData(): Result<List<Article>>

    suspend fun publish(article: Article): Result<Boolean>

    suspend fun getArticles(): Result<List<Article>>

    fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>>

    suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean>

    suspend fun postEvent(event: Event): Result<Boolean>

    suspend fun getAllEvents(): Result<List<Event>>

    fun getLiveEvents(userEmail: String): MutableLiveData<List<Event>>

    fun getLiveMessages(emails: List<String>): MutableLiveData<List<Message>>

    suspend fun updateUser(user: User): Result<Boolean>

    suspend fun getUser(userEmail: String): Result<User>

    fun getLiveUser(userEmail: String): MutableLiveData<List<User>>

    fun getLiveMyEventInvitation(userEmail: String): MutableLiveData<List<Event>>

    suspend fun acceptEvent(event: Event, userEmail: String, userName: String, userImage: String): Result<Boolean>

    suspend fun declineEvent(event: Event, userEmail: String): Result<Boolean>

    suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean>

    suspend fun postUserToFollow(userOwnerEmail: String, user: User): Result<Boolean>

    suspend fun removeUserFromFollow(userEmail: String, user: User): Result<Boolean>

    suspend fun getUserArticle(userEmail: String): Result<List<Article>>

    suspend fun getSavedArticle(userEmail: String): Result<List<Article>>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser>

    suspend fun getImageUri(filePath: String): Result<String>

    suspend fun saveArticle(article: Article, userEmail: String): Result<Boolean>

    suspend fun getFollowing(userEmail: String): Result<List<User>>

    suspend fun getFollowedBy(userEmailList: List<String>): Result<List<User>>

    suspend fun getAllUsers(): Result<List<User>>
}