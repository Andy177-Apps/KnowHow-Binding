package com.wenbin.knowhowbinding.chatroom

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.databinding.ItemChatroomBinding

class ChatRoomAdapter(private val itemChickListener : MessageOnItemClickListener) :
        ListAdapter<ChatRoom,
        ChatRoomAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder (
        private var binding : ItemChatroomBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind (item : ChatRoom) {
            binding.chatRoom = item
//             Chat room has been filtered, the attendee info only holds the other user's info
            Log.d("wenbin", " item.attendeesInfo = ${item.attendeesInfo}")

            val friendInfo = item.attendeesInfo.component1()
            Log.d("wenbin", " friendInfo = $friendInfo")
            binding.textViewObjectName.text = friendInfo.userName
            binding.imageUrl = friendInfo.userImage
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatroomBinding.inflate(layoutInflater,
                    parent, false)
                return ViewHolder(binding)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ViewHolder -> {
                val chatRoom = item as ChatRoom
                holder.bind(chatRoom)
                holder.itemView.setOnClickListener {
                    itemChickListener.onItemClicked(chatRoom)
                }
            }
        }
    }

    class MessageOnItemClickListener(val clickListener : (chatRoom : ChatRoom) -> Unit) {
        fun onItemClicked(chatRoom : ChatRoom) = clickListener(chatRoom)
    }
}