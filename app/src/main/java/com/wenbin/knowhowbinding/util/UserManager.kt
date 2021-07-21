package com.wenbin.knowhowbinding.util

import com.wenbin.knowhowbinding.KnowHowBindingApplication

object UserManager {

    private const val EMAIL = "email"
    private const val EMAIL_VALUE = "email_value"

    var email : String?
        get() {
            return KnowHowBindingApplication.instance.getSharedPreferences(EMAIL, 0)
                    .getString(EMAIL_VALUE,null)
        }
    set(value) {
        KnowHowBindingApplication.instance.getSharedPreferences(EMAIL,0).edit()
                .putString(EMAIL_VALUE,value).apply()
    }
}