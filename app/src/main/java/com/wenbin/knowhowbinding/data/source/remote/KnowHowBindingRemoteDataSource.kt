package com.wenbin.knowhowbinding.data.source.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.util.Logger
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KnowHowBindingRemoteDataSource : KnowHowBindingDataSource {

    private const val PATH_ARTICLES = "articles"
    private const val PATH_CHATROOMLIST = "chatRoomList"
    private const val PATH_MESSAGE = "message"
    private const val PATH_EVENTS = "event"
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
                            Log.d("wembin","article = $article")

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

    override suspend fun getLiveChatRooms(): Result<List<ChatRoom>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_CHATROOMLIST)
                .orderBy("latestTime", Query.Direction.DESCENDING)
//                .whereArrayContains("attenderId", "leo55576")
                .get()
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<ChatRoom>()
                        for (document in task.result!!) {
                            Log.d("Tron", document.id + " => " + document.data)

                            val chatroom1 = ChatRoom()
                            val chatroom = document.toObject(ChatRoom::class.java)
                            Log.d("wenbin", "chatroom = $chatroom")
//                            chatroom1.id = chatroom.id
//                            chatroom1.userImage = chatroom.userImage.filter { it != UserManager.photo}
//                            chatroom1.attenderId = chatroom.attenderId.filter { it != UserManager.email }
//                            chatroom1.attenderName = chatroom.attenderName.filter { it != UserManager.name }
                            list.add(chatroom)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Log.w(
                                    "Tron",
                                    "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                                Result.Fail(
                                        KnowHowBindingApplication.instance.getString(
                                                R.string.you_know_nothing
                                        )
                                )
                        )
                    }
                }
    }

    override suspend fun postMessage(emails: List<String>,
                                     message: Message
    ): Result<Boolean> = suspendCoroutine { continuation ->

    val chat = FirebaseFirestore.getInstance().collection(PATH_CHATROOMLIST)
    chat.whereIn("attendees", listOf(emails, emails.reversed()))
//            .whereEqualTo("id", chatRoom.id)
            .get()
            .addOnSuccessListener { result ->
                val documentId = chat.document(result.documents[0].id)
                documentId
                        //更新文檔的某些字段而不覆蓋整個文檔，請使用update()方法：
                        .update("latestTime", Calendar.getInstance().timeInMillis,
                                "latestMessage", message.text)
            }
        // 用上 Storage 的 continueWithTask
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    if (task.exception != null) {
                        task.exception?.let {
                            Log.d("wenbin", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                    } else {
                        continuation.resume(Result.Fail(KnowHowBindingApplication.appContext.getString(R.string.you_shall_not_pass)))
                    }
            }
                task.result?.let {
                    val documentId2 = chat.document(it.documents[0].id).collection("message").document()

                    message.createdTime = Calendar.getInstance().timeInMillis
                    message.id = documentId2.id

                    chat.document(it.documents[0].id).collection("message").add(message)
                }
            }
            .addOnCompleteListener { taskTwo->
            if (taskTwo.isSuccessful) {
                Logger.i("ChatRoom: $message")

                continuation.resume(Result.Success(true))
            } else {
                taskTwo.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail(KnowHowBindingApplication.appContext.getString(R.string.you_shall_not_pass)))
            }

            }
    }

    override suspend fun postEvent(event: Event): Result<Boolean> = suspendCoroutine { continuation->
        //先與 Firebase 的 collection 連動
        val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
        //再與 Firebase 的 document 連動，方法就是創造該 document
        val document = events.document()

        event.id = document.id
        event.createdTime = Calendar.getInstance().timeInMillis

        document
            .set(event)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("wenbin", "Post: $event")

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

    override suspend fun getAllEvents(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Log.d("wembin",document.id + " => " + document.data)
                        val event = document.toObject(Event::class.java)
                        list.add(event)
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

    override fun getLiveEvents(): MutableLiveData<List<Event>> {

        val liveData = MutableLiveData<List<Event>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " =>>>> " + document.data)

                    val event = document.toObject(Event::class.java)
                    list.add(event)
                }

                liveData.value = list
            }
        return liveData
    }

    // test


}



