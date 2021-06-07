package com.wenbin.knowhowbinding.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentMyselfProfileBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus


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
                findNavController().navigate(ProfileFragmentDirections.navigateToMyArticleFragment())
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

        // Progress Bar
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                }
            }
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Log.d("ProfilePage", "userInfo = $it")
            setupLayout(it)

        })
        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("個人頁面")
        }
        return binding.root
    }

    private fun setupLayout(user: User) {
        val chipTextSize = 20F

        val chipGroupTalented = binding.chipGroupTalentedSubjects
        val genres = user.talentedSubjects

        for (genre in genres) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chipGroupTalented.addView(chip)
        }

        val chipGroupInterested = binding.chipGroupInterestedSubjects
        val interestedList = user.interestedSubjects

        for (genre in interestedList) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chipGroupInterested.addView(chip)
        }

    }
}