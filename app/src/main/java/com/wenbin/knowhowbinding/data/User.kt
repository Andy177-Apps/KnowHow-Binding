package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: String = "",
        val name: String = "",
        val email: String = "",
        var image: String = "",
        var bgImage: String = "",
        var city: String = "尚未設定",
        var district: String = "",
        var gender: String = "尚未公布",
        var tag: List<String> = listOf(),
        var experience: String = "",
        var joinedTime: Long = -1L,
        var followingEmail: List<String> = listOf(),
        var followingName: List<String> = listOf(),
        var followedBy: List<String> = listOf(),
        var following: List<Following> = listOf(),
        var identity: String = "尚未設定",
        val talentedSubjects: List<String> = listOf(),
        val interestedSubjects: List<String> = listOf(),
        val introduction: String = ""
) : Parcelable