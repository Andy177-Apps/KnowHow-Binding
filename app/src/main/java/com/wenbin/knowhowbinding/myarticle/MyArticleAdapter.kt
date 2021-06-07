package com.wenbin.knowhowbinding.myarticle

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.databinding.ItemArticleBinding
import com.wenbin.knowhowbinding.login.UserManager

class MyArticleAdapter(val viewModel: MyArticleViewModel) : ListAdapter<Article,
        MyArticleAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
        private var binding : ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : Article, viewModel: MyArticleViewModel) {
            binding.article = item

            val bookmarkIcon = binding.imageViewBookmark
            binding.imageViewBookmark.setOnClickListener {
                Log.d("saveArticle", "imageViewBookmark is clicked")
                viewModel.saveArticle(item, UserManager.user.email)

                bookmarkIcon.isSelected = !bookmarkIcon.isSelected

//                viewModel.isChecked(bookmarkIcon.isSelected)
            }

            bookmarkIcon.isSelected = item.saveList.contains(UserManager.user.email)

            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemArticleBinding.inflate(layoutInflater,
                    parent, false)
                return ViewHolder(binding)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) as Article, viewModel)
    }

}