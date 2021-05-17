package com.wenbin.knowhowbinding.data

sealed class MessageItem {
    data class Myself(val userId : Message) : MessageItem() {}
    data class OtherSide(val userId : Message) : MessageItem() {}
}