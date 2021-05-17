package com.wenbin.knowhowbinding.chatroom.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wenbin.knowhowbinding.databinding.FragmentMessageBinding
import com.wenbin.knowhowbinding.ext.getVmFactory

class MessageFragment : Fragment() {
    private lateinit var binding : FragmentMessageBinding
    private val viewModel by viewModels<MessageViewModel> { getVmFactory(
            MessageFragmentArgs.fromBundle(requireArguments()).chatRoomInfo
    ) }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = MessageAdapter()
        binding.recyclerView.adapter = adapter

        binding.buttonSendMessage.setOnClickListener {

        }
        return binding.root
    }
}