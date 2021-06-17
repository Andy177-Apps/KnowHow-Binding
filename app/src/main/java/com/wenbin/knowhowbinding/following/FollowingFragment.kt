package com.wenbin.knowhowbinding.following

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.databinding.FragmentFollowingBinding
import com.wenbin.knowhowbinding.ext.getVmFactory

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    private val viewModel by viewModels<FollowingViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(layoutInflater, container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.recyclerViewFollowing.adapter = FollowingAdapter(FollowingAdapter.OnClickListener {
            viewModel.navigateToUserProfile(it)
        })

        viewModel.navigateToUserProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToUserProfileFragment(it.email))
                viewModel.onUserProfileNavigated()
            }
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Log.d("checkFollowing", "userInfo = $it")
        })

        return binding.root
    }
}