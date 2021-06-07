package com.wenbin.knowhowbinding.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        return binding.root
    }
}