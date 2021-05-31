package com.wenbin.knowhowbinding.postarticle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentPostarticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory

class PostArticleFragment : Fragment(){
    private lateinit var binding : FragmentPostarticleBinding
    val viewModel by viewModels<PostArticleViewModel> { getVmFactory() }

//    private val viewModel : PostArticleViewModel by lazy {
//        ViewModelProvider(this).get(PostArticleViewModel::class.java)
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostarticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.buttonSendArticle.setOnClickListener {
            viewModel.publish(viewModel.getArticle())
            findNavController().navigate(PostArticleFragmentDirections.navigateToHomeFragment())
            Log.d("Wenbin", "onClicked")
        }
        

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("發文")
            (activity as MainActivity).coverBottomNav()
        }
//
        return binding.root

    }
}