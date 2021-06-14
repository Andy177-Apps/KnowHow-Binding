package com.wenbin.knowhowbinding.search.searchresult

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.ItemSearchResultBinding

class SearchResultAdapter(private val onClickListener: OnClickListener) :
        ListAdapter<User, SearchResultAdapter.UserViewHolder>(DiffCallback){

    class UserViewHolder(private var binding: ItemSearchResultBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
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
        return UserViewHolder(ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(user)
        }
        Log.d("checkheight", "position = $position")

        val layoutParams = holder.itemView.layoutParams
//        layoutParams.height = 400 + (position % 4) *200

        layoutParams.height = 400 + (position % 4 + 1) *200

//        layoutParams.height = layoutParams.height + (position % 4) *200
        Log.d("checkheight", "layoutParams.height = ${layoutParams.height}")
        holder.itemView.layoutParams = layoutParams
        holder.bind(user)
    }

    class OnClickListener(val clickListener: (user: User) -> Unit) {
        fun onClick(user: User) = clickListener(user)
    }
}