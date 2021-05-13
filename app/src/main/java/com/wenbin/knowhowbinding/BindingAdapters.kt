package com.wenbin.knowhowbinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.chatroom.ChatRoomAdapter
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.home.HomeAdapter

@BindingAdapter("GetArticle")
fun BindRecyclerViewWithArticle(recyclerView: RecyclerView, data : List<Article>?) {
    val adapter = recyclerView.adapter as HomeAdapter
    adapter.submitList(data)
}

@BindingAdapter("ShowMessage")
fun BindRecyclerViewWithMessage(recyclerView: RecyclerView, data : List<Message>?) {
    val adapter = recyclerView.adapter as ChatRoomAdapter
    adapter.submitList(data)
}