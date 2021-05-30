package com.wenbin.knowhowbinding.notify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wenbin.knowhowbinding.databinding.FragmentNotifyBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager

class NotifyFragment: Fragment() {
    private val viewModel by viewModels<NotifyViewModel> { getVmFactory() }

    lateinit var binding: FragmentNotifyBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotifyBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        val adapter = NotifyAdapter(viewModel)

        binding.recyclerNotify.adapter = adapter
        adapter.notifyDataSetChanged()

        viewModel.getLiveAllEventInvitations(UserManager.user.email)
//        Log.d("fragment", "viewModel.allLiveEventInvitations = ${viewModel.allLiveEventInvitations.value} ")

        viewModel.allLiveEventInvitations.observe(viewLifecycleOwner, Observer {
            Log.d("see_what_is_Empty" , "allLiveEventInvitations_1 = $it ")

            if(it.isEmpty()) {
                invitationValueVisibility(false)
            } else {
                invitationValueVisibility(true)
            }

            adapter.submitList(it)
            it?.let {
                Log.d("see_what_is_Empty" , "allLiveEventInvitations_3 = $it ")
                binding.viewModel = viewModel
                Log.d("see_what_is_Empty" , "allLiveEventInvitations_4 = $it ")

            }
            Log.d("see_what_is_Empty" , "allLiveEventInvitations_2 = $it ")
        })
        return binding.root
    }

    private fun invitationValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValue.visibility = View.GONE
            binding.noValueImage.visibility = View.GONE
        } else {
            binding.noValue.visibility = View.VISIBLE
            binding.noValueImage.visibility = View.VISIBLE
        }
    }
}