package com.wenbin.knowhowbinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.chatroom.ChatRoomAdapter
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.Comment
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.home.HomeAdapter
import com.wenbin.knowhowbinding.myarticle.MyArticleAdapter
import com.wenbin.knowhowbinding.mycollect.MyCollectAdapter
import com.wenbin.knowhowbinding.profile.ProfileCommentAdapter
import com.wenbin.knowhowbinding.util.TimeUtil

@BindingAdapter("GetArticle")
fun BindRecyclerViewWithArticle(recyclerView: RecyclerView, data : List<Article>?) {
    data?.let {
        recyclerView.adapter.apply {
            when (this) {
                is HomeAdapter -> submitList(it)
                is MyArticleAdapter -> submitList(it)
                is MyCollectAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("ShowMessage")
fun BindRecyclerViewWithMessage(recyclerView: RecyclerView, data : List<Message>?) {
    val adapter = recyclerView.adapter as ChatRoomAdapter
    adapter.submitList(data)
}

@BindingAdapter("ShowComment")
fun BindRecyclerViewWithComment(recyclerView: RecyclerView, data : List<Comment>?) {
    val adapter = recyclerView.adapter as ProfileCommentAdapter
    adapter.submitList(data)
}

//@BindingAdapter("ShowLatestTime")
//fun bindLatestTime(textView: TextView, time : Long?) {
//    time?.let {  textView.text = TimeUtil.stampTo }
//}