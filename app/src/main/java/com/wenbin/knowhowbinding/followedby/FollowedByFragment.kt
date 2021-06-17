package com.wenbin.knowhowbinding.followedby

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
import com.wenbin.knowhowbinding.databinding.FragmentFollowedbyBinding
import com.wenbin.knowhowbinding.databinding.FragmentFollowingBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager

class FollowedByFragment : Fragment() {
    private lateinit var binding: FragmentFollowedbyBinding

    private val viewModel by viewModels<FollowedByViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowedbyBinding.inflate(layoutInflater, container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.recyclerViewFollowdeBy.adapter = FollowedByAdapter(FollowedByAdapter.OnClickListener {
            viewModel.navigateToUserProfile(it)
        })

        viewModel.navigateToUserProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToUserProfileFragment(it.email))
                viewModel.onUserProfileNavigated()
            }
        })

        viewModel.appOwenerUser.observe(viewLifecycleOwner, Observer {
            Log.d("checkFollowedBy", "appOwenerUser = $it")
            Log.d("checkFollowedBy", "appOwenerUser.followedBy = ${it.followedBy}")

            viewModel.getFollowedBy(it.followedBy)
        })

        return binding.root
    }
}