package com.wenbin.knowhowbinding.chatroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentChatroomBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.home.HomeViewModel

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

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("聊天紀錄")
        }
        return binding.root
    }
}