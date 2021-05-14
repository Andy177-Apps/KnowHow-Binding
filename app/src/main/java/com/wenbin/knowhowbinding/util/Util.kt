package com.wenbin.knowhowbinding.util

import com.wenbin.knowhowbinding.KnowHowBindingApplication

object Util {
//    fun isInternetConnected(): Boolean {
//        val cm = KnowHowBindingApplicatioin.instance
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//        return activeNetwork?.isConnectedOrConnecting == true
//    }

    fun getString(resourceId: Int): String {
        return KnowHowBindingApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return KnowHowBindingApplication.instance.getColor(resourceId)
    }
}