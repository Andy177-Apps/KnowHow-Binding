package com.wenbin.knowhowbinding.chatroom.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMessageBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger

class MessageFragment : Fragment() {
    private lateinit var binding : FragmentMessageBinding
    private val viewModel by viewModels<MessageViewModel> { getVmFactory(
            MessageFragmentArgs.fromBundle(requireArguments()).userEmail,
            MessageFragmentArgs.fromBundle(requireArguments()).userName
    ) }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val myUserEmail = UserManager.user.email
        val friendUserEmail = viewModel.currentChattingUser

        val adapter = MessageAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.liveMessages.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.imageViewSend.setOnClickListener {
            viewModel.sendMessage(myUserEmail, friendUserEmail)
            viewModel.textSend.value = ""
        }

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar(viewModel.currentChattingName)
            (activity as MainActivity).coverBottomNav()
        }

        return binding.root
    }

    override fun onDestroyView() {
        if (activity is MainActivity) {
            (activity as MainActivity).recoverToolBarandBottomNav()
        }
        super.onDestroyView()
    }
}