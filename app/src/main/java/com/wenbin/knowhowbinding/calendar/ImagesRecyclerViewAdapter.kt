package com.wenbin.knowhowbinding.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.databinding.ItemAvatarEventBinding
import com.wenbin.knowhowbinding.util.Logger

class ImagesRecyclerViewAdapter() : ListAdapter<String,
        ImagesRecyclerViewAdapter.ViewHolder>(ImageDiffCallback){


    class ViewHolder (
        private var binding : ItemAvatarEventBinding
    ) : RecyclerView.ViewHolder (binding.root){
        fun bind (item : String) {
            Logger.d("item = $item")
            binding.attendeesImage = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAvatarEventBinding.inflate(layoutInflater,
                    parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object ImageDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}