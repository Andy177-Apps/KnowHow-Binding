package com.wenbin.knowhowbinding.calendar.eventdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.databinding.FragmentEventDetailBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger

class EventDetailFragment : Fragment(){

    private val viewModel by viewModels<EventDetailViewModel> {
        getVmFactory(
            EventDetailFragmentArgs.fromBundle(requireArguments()).event
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        Logger.d("viewModel.event = ${viewModel.event}")


        if(viewModel.event.startTime.equals(-1)){
            binding.layoutStartEndTime.visibility = View.GONE
        } else {
            binding.layoutStartEndTime.visibility = View.VISIBLE
        }

        binding.textAttenddes.text = viewModel.event.attendeesName.first()

        binding.buttonDecline.setOnClickListener {
            viewModel.declineEvent(viewModel.event, UserManager.user.email)
            findNavController().navigate(NavigationDirections.navigateToNotifyFragment())
        }

        binding.buttonAccept.setOnClickListener {
            Logger.d("UserManager.user.image = ${UserManager.user.image}")
            viewModel.acceptEvent(viewModel.event, UserManager.user.email, UserManager.user.name, UserManager.user.image)
            findNavController().navigate(NavigationDirections.navigateToNotifyFragment())
        }

        return binding.root

    }
}