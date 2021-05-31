package com.wenbin.knowhowbinding.util

import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.util.Util.getString

enum class CurrentFragmentType(val value: String) {
    HOME(""),
    CALENDAR(getString(R.string.calendar)),
    CHATROOM(getString(R.string.chatroom)),
    PROFILE(getString(R.string.profile)),
    SEARCH(getString(R.string.search)),
    EDITPROFILE("Profile"),

}