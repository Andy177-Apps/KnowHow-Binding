package com.wenbin.knowhowbinding.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.databinding.ItemArticleBinding
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger

class HomeAdapter(val viewModel: HomeViewModel) : ListAdapter<Article,
        HomeAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
        private var binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item: Article, viewModel: HomeViewModel) {
            binding.article = item
            // When bookmark icon is selected
            val bookmarkIcon = binding.imageViewBookmark
            binding.imageViewBookmark.setOnClickListener {
                viewModel.saveArticle(item, UserManager.user.email)
                bookmarkIcon.isSelected = !bookmarkIcon.isSelected
            }

            bookmarkIcon.isSelected = item.saveList.contains(UserManager.user.email)

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

            // Show and hide content of article
            binding.textViewDescription.setOnClickListener {
                binding.textViewDescription.isSingleLine = false
                binding.buttonCollapse.visibility = View.VISIBLE
            }

            binding.buttonCollapse.setOnClickListener {
                binding.textViewDescription.isSingleLine = true
                binding.buttonCollapse.visibility = View.GONE
            }


            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
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