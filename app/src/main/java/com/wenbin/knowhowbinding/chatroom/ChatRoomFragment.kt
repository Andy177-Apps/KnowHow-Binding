package com.wenbin.knowhowbinding.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.calendar.CalendarViewModel
import com.wenbin.knowhowbinding.data.ChatRoom
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.data.UserInfo
import com.wenbin.knowhowbinding.databinding.FragmentChatroomBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger

class ChatRoomFragment  : Fragment() {
    private lateinit var binding: FragmentChatroomBinding
    val viewModel by viewModels<ChatRoomViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatroomBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ChatRoomAdapter(ChatRoomAdapter.MessageOnItemClickListener{ it ->
            findNavController().navigate(ChatRoomFragmentDirections.navigateToMessageFragment(
                    it.attendeesInfo[0].userEmail, it.attendeesInfo[0].userName))
        })
        binding.recyclerView.adapter = adapter

        // test function
        viewModel.testString.observe(viewLifecycleOwner, Observer {
            Logger.d("testString = $it")
        })


        viewModel.updatedChatRooms.observe(viewLifecycleOwner, Observer {
            // test function

            it?.let {
                it.forEach {chatRoom ->

                    val list = arrayListOf<String>()
                    for (item in chatRoom.attendeesInfo) {
                        Logger.d("UserInfo Email = ${item.userEmail}")
                        list.add(item.userEmail)
                    }
                    viewModel.changer(list)
                }
            }

            it?.let {
                val filteredChatRoom = mutableListOf<ChatRoom>()

                it.forEach { chatRoom ->
                chatRoom.attendeesInfo = excludeMyInfo(chatRoom.attendeesInfo)
                    filteredChatRoom.add(chatRoom)
                }
                viewModel.createFilteredChatRooms(filteredChatRoom)
            }
        })

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("????????????")
        }
        return binding.root
    }

    // Exclude owner information from attendees in ChatRoom documents.
    private fun excludeMyInfo (attendees: List<UserInfo>): List<UserInfo> {
        return attendees.filter {
            it.userEmail != UserManager.user.email
        }
    }
}