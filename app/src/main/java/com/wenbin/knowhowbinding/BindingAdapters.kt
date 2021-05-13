package com.wenbin.knowhowbinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.home.HomeAdapter

@BindingAdapter("GetArticle")
fun BindRecyclerView(recyclerView: RecyclerView, data : List<Article>?) {
    val adapter = recyclerView.adapter as HomeAdapter
    adapter.submitList(data)
}