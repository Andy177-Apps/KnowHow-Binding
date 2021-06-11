package com.wenbin.knowhowbinding

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wenbin.knowhowbinding.calendar.CalendarAdapter
import com.wenbin.knowhowbinding.calendar.ImagesRecyclerViewAdapter
import com.wenbin.knowhowbinding.chatroom.ChatRoomAdapter
import com.wenbin.knowhowbinding.data.*
import com.wenbin.knowhowbinding.followedby.FollowedByAdapter
import com.wenbin.knowhowbinding.following.FollowingAdapter
import com.wenbin.knowhowbinding.home.HomeAdapter
import com.wenbin.knowhowbinding.myarticle.MyArticleAdapter
import com.wenbin.knowhowbinding.mycollect.MyCollectAdapter
import com.wenbin.knowhowbinding.profile.ProfileCommentAdapter
import com.wenbin.knowhowbinding.search.SearchAdapter
import com.wenbin.knowhowbinding.util.TimeUtil
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime

@BindingAdapter("imageUrl")
fun bindImage (imgView: ImageView, imgUrl : String) {
    imgUrl.let {
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
fun bindRecyclerViewWithArticle(recyclerView: RecyclerView, data : List<Article>?) {
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
fun bindRecyclerViewWithMessage(recyclerView: RecyclerView, data : List<ChatRoom>?) {
    val adapter = recyclerView.adapter as ChatRoomAdapter
    adapter.submitList(data)
}

@BindingAdapter("ShowEvent")
fun bindRecyclerViewWithEvent(recyclerView: RecyclerView, data : List<Event>?) {
    data?.let {
        recyclerView.adapter.apply {
            when (this) {
                is CalendarAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("listImage")
fun BindImageRecyclerView(recyclerView: RecyclerView, data : List<String>?) {
    Log.d("checkImage", "child data = $data")

    data?.let {
        recyclerView.adapter.apply {
            Log.d("this", "this = $this")
            when (this) {
                is ImagesRecyclerViewAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("listUserInfo")
fun BindUserInfoRecyclerView(recyclerView: RecyclerView, data : List<User>?) {
    Log.d("checkFollowing", "child data = $data")

    data?.let {
        recyclerView.adapter.apply {
            when (this) {
                is FollowingAdapter -> submitList(it)
                is FollowedByAdapter -> submitList(it)
            }
        }
    }
}


@BindingAdapter("ShowComment")
fun bindRecyclerViewWithComment(recyclerView: RecyclerView, data : List<Comment>?) {
    val adapter = recyclerView.adapter as ProfileCommentAdapter
    adapter.submitList(data)
}

@BindingAdapter("week")
fun bindWeek(textView: TextView, time: Long?) {
    time?.let { textView.text = TimeUtil.stampToWeek(time) }
}

@BindingAdapter("month")
fun bindMonth(textView: TextView, time: Long?) {
    time?.let { textView.text = TimeUtil.stampToMonth(time) }
}

@BindingAdapter("monthInt")
fun bindMonthInt(textView: TextView, time: Long?) {
    time?.let { textView.text = TimeUtil.stampToMothInt(time) }
}

@BindingAdapter("day")
fun bindDay(textView: TextView, time: Long?) {
    time?.let { textView.text = TimeUtil.stampToDayOfMonth(time) }
}

@BindingAdapter("dayInt")
fun bindDayInt(textView: TextView, time: Long?) {
    time?.let { textView.text = TimeUtil.stampToDayInt(time) }
}

@BindingAdapter("time")
fun bindTime(textView: TextView, time: Long?) {
    time?.let { textView.text = TimeUtil.stampToTime(time) }
}

@BindingAdapter("ago")
fun bindAgo(textView: TextView, time:Long?){
    time?.let { textView.text = TimeUtil.stampToAgo(time) }
}

@ExperimentalTime
@BindingAdapter("timeToHrMin")
fun bindTimeToHrMin(text: TextView, time : Long?){
    time?.let {
        text.text = "${SimpleDateFormat("HH:mm").format(time)}"
    }
}
//@BindingAdapter("ShowLatestTime")
//fun bindLatestTime(textView: TextView, time : Long?) {
//    time?.let {  textView.text = TimeUtil.stampTo }
//}