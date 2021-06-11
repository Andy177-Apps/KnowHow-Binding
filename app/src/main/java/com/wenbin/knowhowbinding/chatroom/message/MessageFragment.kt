package com.wenbin.knowhowbinding.chatroom.message

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.data.Message
import com.wenbin.knowhowbinding.databinding.FragmentMessageBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager

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
        Log.d("wenbin","myUserEmail = $myUserEmail, friendUserEmail = $friendUserEmail")

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

    private fun formMessage() : Message {
        return Message(
                id = "leo55576",
                senderName = "Wenbin",
                senderImage = "https://scontent.ftpe2-2.fna.fbcdn.net/v/t1.18169-9/10600632_1420184154908051_3953315238743376164_n.jpg?_nc_cat=109&ccb=1-3&_nc_sid=09cbfe&_nc_ohc=9pzyPuhjvj4AX9nX5hZ&_nc_ht=scontent.ftpe2-2.fna&oh=89a98fa3e4fea279f82cb066100790be&oe=60C7754F",
                text = viewModel.textSend.value!!
        )
    }
}