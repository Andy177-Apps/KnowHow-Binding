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
import com.wenbin.knowhowbinding.calendar.CalendarViewModel
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

        val adapter = ChatRoomAdapter(ChatRoomAdapter.MessageOnItemClickListener{ it ->
            Log.d("check_userInfo", "it = $it")
            Log.d("check_userInfo", "it.attendeesInfo[0].userEmail = ${it.attendeesInfo[0].userEmail}")
//            Log.d("check_userInfo", "it.message!!.senderName = ${it.message!!.senderName}")
            Log.d("check_userInfo", "it.attendeesInfo[0].userName = ${it.attendeesInfo[0].userName}")

            findNavController().navigate(ChatRoomFragmentDirections.navigateToMessageFragment(
                    it.attendeesInfo[0].userEmail, it.attendeesInfo[0].userName))
        })
        binding.recyclerView.adapter = adapter

        ///////test function
        viewModel.testString.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "testString = $it")
        })


        viewModel.updatedChatRooms.observe(viewLifecycleOwner, Observer {
            /////// test function

            Log.d("wenbin", "updatedChatRooms = $it")

            it?.let {
                it.forEach {chatRoom ->
                    Log.d("wenbin", "chatRoom.attendeesInfo = ${chatRoom.attendeesInfo}")

                    val list = arrayListOf<String>()
                    for (item in chatRoom.attendeesInfo) {
                        Log.d("wenbin", "UserInfo Email = ${item.userEmail}")
                        list.add(item.userEmail)
                    }
                    Log.d("wenbin", "New list = $list")
                    viewModel.changer(list)
                }
            }

            fun printListandArray() {
                val list = listOf<String>("1", "2")
                Log.d("wenbin","PrintList list = $list")
            }
            ///////test function
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

    // Exclude owner information from attendees in ChatRoom documents.
    private fun excludeMyInfo (attendees : List<UserInfo>): List<UserInfo> {
        return attendees.filter {
            it.userEmail != UserManager.user.email
        }
    }
}