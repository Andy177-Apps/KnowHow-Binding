package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var id: String = "",
    var city: String = "",
    var title: String = "",
    var description: String = ""

    ) : Parcelable