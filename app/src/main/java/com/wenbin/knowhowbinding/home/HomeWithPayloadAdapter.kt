package com.wenbin.knowhowbinding.home

import android.util.Log
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

class HomeWithPayloadAdapter(val viewModel: HomeViewModel) : ListAdapter<Article,
        HomeWithPayloadAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
        private var binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root){
        private val bookmarkIcon = binding.imageViewBookmark

        fun bind (item: Article, viewModel: HomeViewModel) {
            binding.article = item
            // When bookmark icon is selected
            binding.imageViewBookmark.setOnClickListener {
                viewModel.saveArticle(item, UserManager.user.email)
                bookmarkIcon.isSelected = !bookmarkIcon.isSelected
                Log.d("Wenbin", "item.isFavorite 1= ${item.isFavorite}")
                item.isFavorite = !item.isFavorite
                Log.d("Wenbin", "item.isFavorite 2= ${item.isFavorite}")

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

        fun bindFavoriteState(item: Article, viewModel: HomeViewModel) {
            binding.imageViewBookmark.setOnClickListener {
                viewModel.saveArticle(item, UserManager.user.email)
                bookmarkIcon.isSelected = !bookmarkIcon.isSelected
                item.isFavorite = !item.isFavorite
            }
            Log.d("Wenbin", "bindFavoriteState is used.")
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
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Article, newItem: Article): Any? {
            Log.d("Wenbin", "(oldItem.isFavorite != newItem.isFavorite) = ${(oldItem.isFavorite != newItem.isFavorite)}")
            return if (oldItem.isFavorite != newItem.isFavorite) true else null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /**
         * getItem returns the element at the specified position in this list which is current:ist here (mReadOnlyList).
         * Then, we convert the element to the data type we wanted, e.g. Article.
         * So, setData receives a specified data type element.
         */
        holder.bind(getItem(position) as Article, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        Log.d("Wenbin", "payloads = $payloads")

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (payloads[0] == true) {
                Log.d("Wenbin", "payloads[0] = ${payloads[0]}")

                holder.bindFavoriteState(getItem(position) as Article, viewModel)
            }
        }
    }
}