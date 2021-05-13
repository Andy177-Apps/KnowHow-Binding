package com.wenbin.knowhowbinding.postarticle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentPostarticleBinding

class PostArticleFragment : Fragment(){
    private lateinit var binding : FragmentPostarticleBinding
    private val viewModel : PostArticleViewModel by lazy {
        ViewModelProvider(this).get(PostArticleViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostarticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("發文")
        }
        if (activity is MainActivity) {
            (activity as MainActivity).coverToolBarandBottomNav()
        }
        return binding.root

    }
}