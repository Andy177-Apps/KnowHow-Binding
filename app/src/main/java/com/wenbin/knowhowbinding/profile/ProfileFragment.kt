package com.wenbin.knowhowbinding.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMyselfProfileBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.profile.editprofile.EditProfileViewModel


class ProfileFragment  : Fragment() {
    private lateinit var binding : FragmentMyselfProfileBinding
    val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

//    private val viewModel : ProfileViewModel by lazy {
//        ViewModelProvider(this).get(ProfileViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("wenbin","UserManager.user = ${UserManager.user}")

        binding = FragmentMyselfProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var adapter = ProfileCommentAdapter()
        binding.recyclerView.adapter = adapter

        // Navigating to My Article Fragment.
        viewModel.navigateToMyArticle.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navigateToMyArticleFragment(UserManager.user.email))
                viewModel.onMyArticleNavigated()
            }
        })

        // Navigating to My Collect Fragment.
        viewModel.navigateToMyCollect.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navigateToMyCollectFragment())
                viewModel.onMyCollectNavigated()
            }
        })

        // Navigating to Edit Profile Fragment.
        viewModel.navigateToEditProfile.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navigateToEditProfile())
                viewModel.onEditProfileNavigated()
            }
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Log.d("ProfilePage", "userInfo = $it")
        })
        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("個人頁面")
        }
        return binding.root
    }
}