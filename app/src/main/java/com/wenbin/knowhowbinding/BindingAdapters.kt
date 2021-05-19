package com.wenbin.knowhowbinding

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wenbin.knowhowbinding.chatroom.ChatRoomAdapter
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.Comment
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.home.HomeAdapter
import com.wenbin.knowhowbinding.myarticle.MyArticleAdapter
import com.wenbin.knowhowbinding.mycollect.MyCollectAdapter
import com.wenbin.knowhowbinding.profile.ProfileCommentAdapter
import com.wenbin.knowhowbinding.search.SearchAdapter
import com.wenbin.knowhowbinding.util.TimeUtil

@BindingAdapter("imageUrl")
fun bindImage (imgView: ImageView, imgUrl : String) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("GetArticle")
fun BindRecyclerViewWithArticle(recyclerView: RecyclerView, data : List<Article>?) {
    data?.let {
        recyclerView.adapter.apply {
            when (this) {
                is HomeAdapter -> submitList(it)
                is MyArticleAdapter -> submitList(it)
                is MyCollectAdapter -> submitList(it)
                is SearchAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("ShowMessage")
fun BindRecyclerViewWithMessage(recyclerView: RecyclerView, data : List<ChatRoom>?) {
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