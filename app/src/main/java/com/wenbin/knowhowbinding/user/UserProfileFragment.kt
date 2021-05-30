package com.wenbin.knowhowbinding.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.calendar.eventdetail.EventDetailFragmentArgs
import com.wenbin.knowhowbinding.calendar.eventdetail.EventDetailViewModel
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentUserDetailBinding
import com.wenbin.knowhowbinding.ext.getVmFactory

class UserProfileFragment: Fragment() {
    private lateinit var binding: FragmentUserDetailBinding

    private val viewModel by viewModels<UserProfileViewModel> {
        getVmFactory(
                UserProfileFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var firstTimeEntry = true

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Log.d("check_userInfo", "accepted userInfo = $it")

            if (firstTimeEntry) {
                setupLayout(it)
                firstTimeEntry = false
            }
        })
        return binding.root
    }

    private fun setupLayout(user: User) {
        val chipGroup = binding.chipGroupTalentedSubjects
        val genres = user.tag

        for (genre in genres) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chipGroup.addView(chip)
        }

        binding.buttonMessage.setOnClickListener {
            viewModel.postChatRoom(viewModel.createChatRoom())
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(NavigationDirections.navigateToMessageFragment(user.email, user.name))
            }, 3000)
        }

    }
}