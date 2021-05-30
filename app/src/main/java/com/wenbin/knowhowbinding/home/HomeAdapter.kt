package com.wenbin.knowhowbinding.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.databinding.ItemArticleBinding
import com.wenbin.knowhowbinding.login.UserManager

class HomeAdapter : ListAdapter<Article,
        HomeAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
        private var binding : ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : Article) {
            binding.article = item
            binding.constraintLayoutUserInformation.setOnClickListener {

                item.author?.let {
                    if (item.author.email == UserManager.user.email) {
                        Navigation.createNavigateOnClickListener(NavigationDirections.navigateToProfileFragment()).
                        onClick(binding.constraintLayoutUserInformation)
                    } else {
                        Navigation.createNavigateOnClickListener(NavigationDirections.navigateToUserProfileFragment(item.author.email)).
                        onClick(binding.constraintLayoutUserInformation)
                    }
                }
            }

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
        holder.bind(getItem(position) as Article)
    }

}