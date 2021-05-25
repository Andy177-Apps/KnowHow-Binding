package com.wenbin.knowhowbinding.profile.editprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentEditprofileBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import androidx.lifecycle.Observer
import com.wenbin.knowhowbinding.login.UserManager


class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditprofileBinding
    val viewModel by viewModels<EditProfileViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditprofileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Navigating to Profile Fragment.
        viewModel.navigateToProfilePage.observe(viewLifecycleOwner, Observer{
            it?.let {
                val observeIdentity = viewModel.getUser()
                Log.d("EditPage", "observeIdentity = $observeIdentity")

                viewModel.updateUser(viewModel.getUser())
                findNavController().navigate(EditProfileFragmentDirections.navigateToProfileFragment())
                viewModel.onProfilePageNavigated()
            }
        })

        viewModel.identity.observe(viewLifecycleOwner, Observer {
            Log.d("EditPage", "identity = $it")
        })

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("編輯個人頁面")
        }

        return binding.root
    }
}