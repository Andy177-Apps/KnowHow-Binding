package com.wenbin.knowhowbinding.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.ItemCommentBinding
import com.wenbin.knowhowbinding.databinding.ItemRecommendedUserBinding

class ProfileRecommendedAdapter : ListAdapter<User,
        ProfileRecommendedAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
            private var binding : ItemRecommendedUserBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : User) {
            binding.user = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecommendedUserBinding.inflate(layoutInflater,
                        parent, false)
                return ViewHolder(binding)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) as User)
    }

}