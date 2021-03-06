package com.wenbin.knowhowbinding.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.MainViewModel
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentMyselfProfileBinding
import com.wenbin.knowhowbinding.ext.excludeOwner
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.ext.recommendedUser
import com.wenbin.knowhowbinding.followedby.FollowedByAdapter
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyselfProfileBinding
    val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyselfProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getAllUsers()

        val mainViewModel = ViewModelProvider(this.requireActivity()).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel

        mainViewModel.getUser(UserManager.user.email)
        mainViewModel.getUserArticle(UserManager.user.email)
        
        val adapter = ProfileRecommendedAdapter(ProfileRecommendedAdapter.OnClickListener {
            viewModel.navigateToUserProfile(it)
        })
        binding.recyclerView.adapter = adapter

        mainViewModel.userArticles.observe(viewLifecycleOwner, Observer { list ->
            binding.textPosts.text = list.size.toString()
        })

        // Filter recommended users
        viewModel.allUsers.observe(viewLifecycleOwner, Observer {
            var resultList = listOf<User>()

            viewModel.userInfo.value?.let { ownerUser ->
                resultList = it.recommendedUser(ownerUser).excludeOwner()
            }
            Logger.d("checkRecommendedList, Final resultList in fragment = $resultList")
            adapter.submitList(resultList)
        })

        binding.imageViewExpand.setOnClickListener {
            if (binding.layoutRecommended.visibility == View.GONE) {
                binding.layoutRecommended.visibility = View.VISIBLE
            } else {
                binding.layoutRecommended.visibility = View.GONE
            }
        }

        // Navigating to My UserProfile Fragment.
        viewModel.navigateToUserProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToUserProfileFragment(it.email))
                viewModel.onUserProfileNavigated()
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
            Logger.d("ProfilePage, userInfo = $it")
            setupLayout(it)
        })
        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("????????????")
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

        val chipGroupInterested = binding.chipGroupInterestedSubject
        val interestedList = user.interestedSubjects

        for (genre in interestedList) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chipGroupInterested.addView(chip)
        }
    }
}