package com.wenbin.knowhowbinding.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wenbin.knowhowbinding.databinding.FragmentUserDetailBinding

class UserProfileFragment: Fragment() {
    private lateinit var binding: FragmentUserDetailBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater)
        return binding.root
    }
}