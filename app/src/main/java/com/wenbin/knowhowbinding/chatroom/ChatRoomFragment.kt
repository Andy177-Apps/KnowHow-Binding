package com.wenbin.knowhowbinding.chatroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.UserInfo
import com.wenbin.knowhowbinding.databinding.FragmentChatroomBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager

class ChatRoomFragment  : Fragment() {
    private lateinit var binding : FragmentChatroomBinding
    val viewModel by viewModels<ChatRoomViewModel> { getVmFactory() }

//    private val viewModel : ChatRoomViewModel by lazy {
//        ViewModelProvider(this).get(ChatRoomViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatroomBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var adapter = ChatRoomAdapter(ChatRoomAdapter.MessageOnItemClickListener{
            Log.d("Message Clicked", "ChatRoom = $it")
            findNavController().navigate(ChatRoomFragmentDirections.navigateToMessageFragment(
                    it.message!!.senderEmail, it.message!!.senderName))
        })
        binding.recyclerView.adapter = adapter

        viewModel.updatedChatRooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                val filteredChatRoom = mutableListOf<ChatRoom>()

                it.forEach { chatRoom ->
                    Log.d("werbin", "original chatRoom.attendeesInfo = ${chatRoom.attendeesInfo}")
                chatRoom.attendeesInfo = excludeMyInfo(chatRoom.attendeesInfo)
                    Log.d("werbin", "filtered chatRoom.attendeesInfo = ${chatRoom.attendeesInfo}")


                    filteredChatRoom.add(chatRoom)
                }
                viewModel.createFilteredChatRooms(filteredChatRoom)
            }
        })
        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("聊天紀錄")
        }
        return binding.root
    }

    private fun excludeMyInfo (attendees : List<UserInfo>): List<UserInfo> {
        return attendees.filter {
            it.userEmail != UserManager.user.email
        }
    }
}