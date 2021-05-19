package com.wenbin.knowhowbinding.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    /**
     * This singleton converts timestamp to required format
     * Be advised the timestamp contains milliseconds
     */
//    fun stampToAgo(time : Long) : String {
//        return TimeAgo.using(time)
//    }

    @JvmStatic
    fun stampToDate(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(Date(time))
    }
    @JvmStatic
    fun stampToYear(time : Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToMothInt(time : Long): String {
        val simpleDateFormat = SimpleDateFormat("MM")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToDay(time : Long): String {
        val simpleDateFormat = SimpleDateFormat("dd")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun dateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun timeToStamp(time: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("HH:mm", locale)
        return simpleDateFormat.parse(time).time
    }


}