package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
        var id: String = "",
        var tag: String = "",
        var type: String = "",
        var createdTime: Long = -1,
        var title: String = "",
        var city: String ="",
        var find: String = "",
        var give: String = "",
        var content: String = "",
        val saveList: List<String> = listOf(),
        val author: User? = null
) : Parcelable