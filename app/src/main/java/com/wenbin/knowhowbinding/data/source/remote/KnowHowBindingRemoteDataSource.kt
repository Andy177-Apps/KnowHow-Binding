package com.wenbin.knowhowbinding.data.source.remote

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.login.LoginActivity
import com.wenbin.knowhowbinding.util.Logger
import java.io.File
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object KnowHowBindingRemoteDataSource : KnowHowBindingDataSource {

    private const val PATH_ARTICLES = "articles"
    private const val PATH_CHATROOMLIST = "chatRoomList"
    private const val PATH_MESSAGE = "message"
    private const val PATH_EVENTS = "event"
    private const val PATH_USERS = "users"
    private const val KEY_CREATED_TIME = "createdTime"

    // Set up Cloud Storage
    // Create an instance of FirebaseStorage to access my Cloud Storage bucket
    private val storage = Firebase.storage

    // Create a reference thought of as a pointer to a file in the cloud.
    private var storageRef = storage.reference

    override suspend fun getImageUri(filePath: String): Result<String> =
        suspendCoroutine { continuation ->

            var file = Uri.fromFile(File(filePath))
            val riversRef = storageRef.child("images/${file.lastPathSegment}")
            val uploadTask = riversRef.putFile(file)

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                Log.d("checkStorage", "Error getting documents in DataSource:  ", it)

                continuation.resume(Result.Error(it))
            }.addOnSuccessListener { taskSnapshot ->
                val storagePath = taskSnapshot.metadata?.path as String

                // Immediately download
                storageRef.child(storagePath)
                    .downloadUrl
                    .addOnSuccessListener {
                        // Got the download URL for storagePath
                        continuation.resume(Result.Success(it.toString()))

                    }.addOnFailureListener {
                        continuation.resume(Result.Error(it))
                    }
            }
                .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    if (progress < 100) {
//                        continuation.resume(Result.Progress(progress))
                    }
            }
        }

    override suspend fun login(id: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createTestedData(): Result<List<Article>> {
        TODO("not implemented")
    }

    override suspend fun publish(article: Article): Result<Boolean> =
        suspendCoroutine { continuation ->
            val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
            val document = articles.document()

            article.id = document.id
            article.createdTime = Calendar.getInstance().timeInMillis

            document
                .set(article)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("wenbin", "Publish: $article")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "wenbin",
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

    override suspend fun getArticles(): Result<List<Article>> = suspendCoroutine { continuation ->
        Log.d("wembin", "getArticles is used")

        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
//            .whereEqualTo("author.id","leo555")
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Article>()
                    for (document in task.result!!) {
                        Log.d("wembin", document.id + " => " + document.data)
                        val article = document.toObject(Article::class.java)
                        Log.d("wembin", "article_list = $article")

                        list.add(article)
                    }
                    Log.d("wembin", "list = $list")
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Log.w(
                            "Wenbin",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>> {

        val liveData = MutableLiveData<List<ChatRoom>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_CHATROOMLIST)
            .orderBy("latestTime", Query.Direction.DESCENDING)
            .whereArrayContains("attendees", userEmail)
            .addSnapshotListener { snapshot, exception ->
                Logger.i("add SnapshotListener detected")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<ChatRoom>()
                snapshot?.forEach { document ->
                    Logger.d(document.id + " => " + document.data)

                    val chatRoom = document.toObject(ChatRoom::class.java)
                    Log.d("wenbin", "chatRoom = $chatRoom")

                    list.add(chatRoom)
                }
                liveData.value = list

            }
        return liveData
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> =
        suspendCoroutine { continuation ->
            val chatRooms = FirebaseFirestore.getInstance().collection(PATH_CHATROOMLIST)
            val document = chatRooms.document()

            chatRoom.id = document.id
            chatRoom.latestTime = Calendar.getInstance().timeInMillis

            chatRooms
                .whereIn("attendees", listOf(chatRoom.attendees, chatRoom.attendees.reversed()))
                .get()
                .addOnSuccessListener { result ->
                    //這是在 ChatRoomList 創造與那個人的 document，如果沒有那個 document 才創建，有的話就不創了
                    if (result.isEmpty) {
                        document
                            .set(chatRoom)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Logger.i("Chatroom: $chatRoom")

                                    continuation.resume(Result.Success(true))
                                } else {
                                    task.exception?.let {

                                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                        continuation.resume(Result.Error(it))
                                        return@addOnCompleteListener
                                    }
                                    continuation.resume(
                                        Result.Fail(
                                            KnowHowBindingApplication.appContext.getString(
                                                R.string.you_shall_not_pass
                                            )
                                        )
                                    )
                                }
                            }
                    } else {
                        for (myDocument in result) {
                            Logger.d("Already initialized")
                        }
                    }
                }

        }

    override suspend fun postUserToFollow(userOwnerEmail: String, user: User): Result<Boolean> =
        suspendCoroutine { continuation ->

//        val users = FirebaseFirestore.getInstance().collection(PATH_USERS)
//
//        users.document(userOwnerEmail).collection("followList").document(user.email)
//                .set(user)
//                .addOnSuccessListener {
//                    Logger.d("DocumentSnapshot added with ID: $users")
//                }
//                .addOnFailureListener { e ->
//                    Logger.w("Error adding document $e")
//                }
//        users.document(user.email).update("followedBy", FieldValue.arrayUnion(userOwnerEmail))
//        users.document(userOwnerEmail).update("followingEmail", FieldValue.arrayUnion(user.email))
//        users.document(userOwnerEmail).update("followingName", FieldValue.arrayUnion(user.name))

            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            Log.d("RemoteupdateUser", "user.email = ${user.email}")

            db
                .whereEqualTo("email", userOwnerEmail)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //發出邀請
                        val updateUserInfo = db.document(document.id)
                        // Set the "isCapital" field of the city 'DC'
                        updateUserInfo.update("followingEmail", FieldValue.arrayUnion(user.email))
                        updateUserInfo.update("followingName", FieldValue.arrayUnion(user.name))
                        updateUserInfo.update(
                            "following",
                            FieldValue.arrayUnion(
                                Following(
                                    userEmail = user.email,
                                    userName = user.name
                                )
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }

            db
                .whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //發出邀請
                        val updateUserInfo = db.document(document.id)
                        // Set the "isCapital" field of the city 'DC'
                        updateUserInfo.update("followedBy", FieldValue.arrayUnion(userOwnerEmail))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }

        }

    override suspend fun removeUserFromFollow(userOwnerEmail: String, user: User): Result<Boolean> =
        suspendCoroutine { continuation ->
//        val users = FirebaseFirestore.getInstance().collection(PATH_USERS)
//
//        users.document(userOwnerEmail).collection("followList").document(user.email)
//                .delete()
//                .addOnSuccessListener {
//                    Logger.d("DocumentSnapshot added with ID: ${users}")
//                }
//                .addOnFailureListener { e ->
//                    Logger.w("Error adding document $e")
//                }
//        users.document(user.email).update("followedBy", FieldValue.arrayRemove(userOwnerEmail))
//        users.document(userOwnerEmail).update("followingEmail", FieldValue.arrayRemove(user.email))
//        users.document(userOwnerEmail).update("followingName", FieldValue.arrayRemove(user.name))

            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            Log.d("RemoteupdateUser", "user.email = ${user.email}")

            db
                .whereEqualTo("email", userOwnerEmail)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //發出邀請
                        val updateUserInfo = db.document(document.id)
                        // Set the "isCapital" field of the city 'DC'
                        updateUserInfo.update("followingEmail", FieldValue.arrayRemove(user.email))
                        updateUserInfo.update("followingName", FieldValue.arrayRemove(user.name))
                        updateUserInfo.update(
                            "following",
                            FieldValue.arrayUnion(
                                Following(
                                    userEmail = user.email,
                                    userName = user.name
                                )
                            )
                        )

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }

            db
                .whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val updateUserInfo = db.document(document.id)
                        updateUserInfo.update("followedBy", FieldValue.arrayRemove(userOwnerEmail))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }
        }

    override suspend fun getUserArticle(userEmail: String): Result<List<Article>> =
        suspendCoroutine { continuation ->
            Log.d("check_userArticles", "getUserArticle in DataSource is used.")

            val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)

            articles
//                .whereEqualTo("creatorEmail", userEmail)
                .whereEqualTo("author.email", userEmail)
//                .orderBy("author").whereEqualTo("email", userEmail)
//                .whereGreaterThanOrEqualTo("author",userEmail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Article>()
                        task.result?.forEach { document ->
                            Logger.d(document.id + " => " + document.data)

                            val article = document.toObject(Article::class.java)
                            list.add(article)
                        }

                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                KnowHowBindingApplication.appContext.getString(
                                    R.string.you_shall_not_pass
                                )
                            )
                        )
                    }

                }
        }

    override suspend fun postUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
        val document = db.document(user.id)
        Log.d("checkUser", "user in DataSource = $user")
        Log.d("checkUser", "user.id in DataSource = ${user.id}")
        Log.d("checkUser", "user.email in DataSource = ${user.email}")

        db.whereEqualTo("email", user.email)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("documents", " Already initialized")
                Log.d("documents", "documents = ${documents.isEmpty}}")

                for (document in documents) {
                    Log.d(
                        "documents",
                        "Received in DataSource = ${document.id} => ${document.data}"
                    )
                }
                if (documents.isEmpty) {
                    document
                        .set(user)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                            Log.d("checkUser", "User in addOnSuccessListener = $user")

                            Logger.i("User: $user")
//                            continuation.resume(Result.Success(true))
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")
//                            continuation.resume(Result.Error(e))
                        }
//                    continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("documents", "Error getting documents: ", exception)
            }

//        db.whereEqualTo("email", user.email)
//            .get()
//            .addOnSuccessListener { documents ->
//                Log.d("postUser","Already initialized")
//                for (document in documents) {
//                    Log.d("checkUser", "in DataSource ： ${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("postUser","Yet initialized")
//
//                document
//                    .set(user)
//                    .addOnSuccessListener {
//                        Log.d(TAG, "DocumentSnapshot successfully written!")
//                        Log.d("checkUser", "User in addOnSuccessListener = $user")
//
//                        Logger.i("User: $user")
//                        continuation.resume(Result.Success(true))
//                    }
//                    .addOnFailureListener { e ->
//                             Log.w(TAG, "Error writing document", e)
//                        Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")
//                        continuation.resume(Result.Error(e))
//                    }
//                continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_know_nothing)))
//
//            }
    }

    override suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser> =
        suspendCoroutine { continuation ->
            Log.d("check_googleSign", "firebaseAuthWithGoogle in DataSource is used.")
            Log.d("check_googleSign", "idToken = $idToken")
            val auth = Firebase.auth

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(LoginActivity.TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        user?.let {
                            continuation.resume(Result.Success(it))
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(LoginActivity.TAG, "signInWithCredential:failure", task.exception)
                        task.exception?.let {
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

    override suspend fun postMessage(
        emails: List<String>,
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
                    .update(
                        "latestTime", Calendar.getInstance().timeInMillis,
                        "latestMessage", message.text
                    )
            }
            // 用上 Storage 的 continueWithTask
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    if (task.exception != null) {
                        task.exception?.let {
                            Log.d(
                                "wenbin",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                        }
                    } else {
                        continuation.resume(
                            Result.Fail(
                                KnowHowBindingApplication.appContext.getString(
                                    R.string.you_shall_not_pass
                                )
                            )
                        )
                    }
                }
                task.result?.let {
                    val documentId2 =
                        chat.document(it.documents[0].id).collection("message").document()

                    message.createdTime = Calendar.getInstance().timeInMillis
                    message.id = documentId2.id

                    chat.document(it.documents[0].id).collection("message").add(message)
                }
            }
            .addOnCompleteListener { taskTwo ->
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

    override suspend fun postEvent(event: Event): Result<Boolean> =
        suspendCoroutine { continuation ->
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

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "wenbin",
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

    override suspend fun getAllEvents(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Log.d("wembin", document.id + " => " + document.data)
                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Log.w(
                            "Wenbin",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
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

    override fun getLiveMessages(emails: List<String>): MutableLiveData<List<Message>> {

        val liveData = MutableLiveData<List<Message>>()

        val chat = FirebaseFirestore.getInstance().collection(PATH_CHATROOMLIST)
        chat.whereIn("attendees", listOf(emails, emails.reversed()))
            .get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        return@addOnCompleteListener
                    }
                }

                task.result?.let {
                    chat.document(it.documents[0].id)
                        .collection(PATH_MESSAGE)
                        .orderBy(KEY_CREATED_TIME)
                        .addSnapshotListener { snapshot, exception ->
                            Logger.i("addSnapshotListener detect")

                            if (snapshot != null) {
                                Log.d("outbounder", "snapshot = ${snapshot.documents}")
                            } else {
                                Log.d("outbounder", "snapshot = null")
                            }


                            exception?.let {
                                Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            }

                            val list = mutableListOf<Message>()

                            for (document in snapshot!!) {
                                Logger.d(document.id + " => " + document.data)
                                Log.d("outbounder", "${document.data}")
                                val message = document.toObject(Message::class.java)
                                list.add(message)
                            }

                            liveData.value = list
                            Logger.w("liveData.value = ${liveData.value}")
                        }
                }
            }
        return liveData
    }

    override suspend fun updateUser(user: User): Result<Boolean> = suspendCoroutine {
        val db = FirebaseFirestore.getInstance()
        Log.d("RemoteupdateUser", "user.email = ${user.email}")
        Log.d("RemoteupdateUser", "user.image = ${user.image}")

        db.collection(PATH_USERS)
            .whereEqualTo("email", user.email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    //發出邀請
                    val updateUserInfo = db.collection(PATH_USERS).document(document.id)
                    // Set the "isCapital" field of the city 'DC'
                    updateUserInfo.update(
                        "identity", user.identity,
                        "talentedSubjects", user.talentedSubjects,
                        "city", user.city,
                        "interestedSubjects", user.interestedSubjects,
                        "introduction", user.introduction,
                        "image", user.image
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }


        // 更新資料
//        users.
//
//                .addOnSuccessListener {
//                    Logger.d("DocumentSnapshot added with ID: $users")
//                }
//                .addOnFailureListener { e ->
//                    Logger.w("Error adding document $e")
//                }
    }

    override suspend fun getUser(userEmail: String): Result<User> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var list = User()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)
                            list = user
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
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

    override fun getLiveMyEventInvitation(userEmail: String): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .whereArrayContains("invitation", userEmail)
            .addSnapshotListener { snapshot, exception ->
                Logger.i("add SnapshotListener detected")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                snapshot?.forEach { document ->
                    Logger.d(document.id + " => " + document.data)

                    val event = document.toObject(Event::class.java)
                    Log.d("check_event", "event = $event")

                    list.add(event)
                }
//                if (snapshot != null) {
//                    for (document in snapshot) {
//                        Logger.d(document.id + " => " + document.data)
//
//                        val event = document.toObject(Event::class.java)
//                        list.add(event)
//                    }
//                }


                liveData.value = list
                Log.d("check_liveevents", "liveData.value = ${liveData.value}")

            }

        return liveData
    }

    override suspend fun acceptEvent(
        event: Event,
        userEmail: String,
        userName: String
    ): Result<Boolean> = suspendCoroutine { continuation ->
        val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
        val document = events.document(event.id)
        var success = 0

        document
            .update("invitation", FieldValue.arrayRemove(userEmail))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("Post: $event")

                    success++

                    if (success == 3) {
                        continuation.resume(Result.Success(true))
                    } else {
                        Logger.i("Still got work to do")
                    }

                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_shall_not_pass)))
                }
            }

        document
            .update("attendees", FieldValue.arrayUnion(userEmail))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("Post: $event")

                    success++

                    if (success == 3) {
                        continuation.resume(Result.Success(true))
                    } else {
                        Logger.i("Still got work to do")
                    }

                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_shall_not_pass)))
                }
            }

        document
            .update("attendeesName", FieldValue.arrayUnion(userName))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("Post: $event")

                    success++

                    if (success == 3) {
                        continuation.resume(Result.Success(true))
                    } else {
                        Logger.i("Still got work to do")
                    }

                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(KnowHowBindingApplication.instance.getString(R.string.you_shall_not_pass)))
                }
            }
    }

    override suspend fun declineEvent(event: Event, userEmail: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
            val document = events.document(event.id)

            document
                .update("invitation", FieldValue.arrayRemove(userEmail))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Post: $event")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                KnowHowBindingApplication.instance.getString(
                                    R.string.you_shall_not_pass
                                )
                            )
                        )
                    }
                }
        }
}



