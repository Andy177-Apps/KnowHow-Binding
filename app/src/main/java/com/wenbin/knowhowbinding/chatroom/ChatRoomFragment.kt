package com.wenbin.knowhowbinding.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentChatroomBinding

class ChatRoomFragment  : Fragment() {
    private lateinit var binding : FragmentChatroomBinding
    private val viewModel : ChatRoomViewModel by lazy {
        ViewModelProvider(this).get(ChatRoomViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatroomBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("聊天紀錄")
        }
        return binding.root
    }
}