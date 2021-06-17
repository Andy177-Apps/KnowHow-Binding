package com.wenbin.knowhowbinding.util

import com.github.marlonlom.utilities.timeago.TimeAgo
import com.wenbin.knowhowbinding.KnowHowBindingApplication

object Util {

    fun getString(resourceId: Int): String {
        return KnowHowBindingApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return KnowHowBindingApplication.instance.getColor(resourceId)
    }

}