package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id : String = "",
        val name: String = "",
        val email: String = "",
        var image: String = "",
        var city: String = "",
        var district: String = "",
        var gender: String = "",
        var tag: List<String> = listOf(),
        var experience: String = "",
        var joinedTime: Long = -1L,
        var followingEmail: List<String> = listOf(),
        var followingName: List<String> = listOf(),
        var followedBy: List<String> = listOf(),

        var identity: String = "",

        val talentedSubjects: String = "",

        val interestedSubjects: String = "",

        val introduction: String = ""
//        val comment : Comment
) : Parcelable

@Parcelize
data class Comment(
    val poster_id : String,
    val description: String
) : Parcelable