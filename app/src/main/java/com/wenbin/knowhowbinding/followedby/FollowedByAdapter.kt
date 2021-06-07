package com.wenbin.knowhowbinding.followedby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.ItemFollowingBinding

class FollowedByAdapter(private val onClickListener: OnClickListener) :
        ListAdapter<User, FollowedByAdapter.UserViewHolder>(DiffCallback){

    class UserViewHolder(private var binding: ItemFollowingBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(user : User) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(user)
        }
        holder.bind(user)
    }

    class OnClickListener(val clickListener: (user: User) -> Unit) {
        fun onClick(user: User) = clickListener(user)
    }
}