package com.wenbin.knowhowbinding.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.databinding.ItemEventBinding

class CalendarAdapter : ListAdapter<Event,
        CalendarAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
            private var binding : ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : Event) {
            binding.event = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEventBinding.inflate(layoutInflater,
                        parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) as Event)
    }

}