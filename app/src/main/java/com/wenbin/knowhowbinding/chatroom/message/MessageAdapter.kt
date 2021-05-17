package com.wenbin.knowhowbinding.chatroom.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.data.MessageItem
import com.wenbin.knowhowbinding.databinding.ItemMessageMyselfBinding
import com.wenbin.knowhowbinding.databinding.ItemMessageOtherSideBinding

class MessageAdapter() : ListAdapter<MessageItem,
        RecyclerView.ViewHolder> (DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }
        private const val ITEM_VIEW_TYPE_MYSELF     = 0x00
        private const val ITEM_VIEW_TYPE_OTHER_SIDE = 0x01
    }

    class MyselfViewHolder (
            private var binding : ItemMessageMyselfBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : Message) {
            binding.message = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup) : MyselfViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageMyselfBinding.inflate(layoutInflater,
                        parent, false)
                return MyselfViewHolder(binding)
            }
        }
    }

    class OtherSideViewHolder (
            private var binding : ItemMessageOtherSideBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : Message) {
            binding.message = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup) : OtherSideViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageOtherSideBinding.inflate(layoutInflater,
                        parent, false)
                return OtherSideViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_MYSELF -> MyselfViewHolder(ItemMessageMyselfBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            ITEM_VIEW_TYPE_OTHER_SIDE -> OtherSideViewHolder(ItemMessageOtherSideBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MyselfViewHolder -> {
                var message : Message? = null
                when (item) {
                    is MessageItem.Myself -> {
                        message = item.userId
                    }
                }
                if (message != null) {
                    holder.bind(message)
                }
            }
            is OtherSideViewHolder -> {
                var message : Message? = null
                when (item) {
                    is MessageItem.OtherSide -> {
                        message = item.userId
                    }
                }
                if (message != null) {
                    holder.bind(message)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is MessageItem.Myself -> ITEM_VIEW_TYPE_MYSELF
            is MessageItem.OtherSide -> ITEM_VIEW_TYPE_OTHER_SIDE
        }
    }
}

