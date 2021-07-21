package com.wenbin.knowhowbinding.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentUserDetailBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger

class UserProfileFragment: Fragment() {

    private lateinit var binding: FragmentUserDetailBinding

    private val viewModel by viewModels<UserProfileViewModel> {
        getVmFactory(
                UserProfileFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val imageViewGender = binding.imageViewGender
        var firstTimeEntry = true

        viewModel.getMyUserInfo(UserManager.user.email)


        viewModel.userInfo.observe(viewLifecycleOwner, Observer {

            imageViewGender.isSelected = it.gender == "male"

            if (it.email != "") {
                if (firstTimeEntry) {
                    setupLayout(it)
                    firstTimeEntry = false
                }
            }

            // Show the count of articles of the user.
            viewModel.userArticles.observe(viewLifecycleOwner, Observer { list ->
                Logger.d("Check_userArticles, userArticles = $list")
                binding.textPosts.text = list.size.toString()
            })

            if (activity is MainActivity) {
                Logger.d("user fun is used.")
                (activity as MainActivity).coverBottomNav()
                    (activity as MainActivity).resetToolBar(it.name)
            }
        })

        // follow
        viewModel.myInfo.observe(viewLifecycleOwner, Observer { my ->

            if (my.followingEmail.contains(viewModel.selectedUserEmail)) {
                showFollowButton(false)
            } else {
                showFollowButton(true)
            }
        })

        binding.buttonUnfollow.setOnClickListener {
            viewModel.removeUserFromFollow(UserManager.user.email, viewModel.userInfo.value!!)
            showFollowButton(true)
        }
        binding.buttonFollow.setOnClickListener {
            viewModel.postUserToFollow(UserManager.user.email, viewModel.userInfo.value!!)
            showFollowButton(false)
        }

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

        // Navigating to My Article Fragment.
        viewModel.navigateToMyArticle.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(UserProfileFragmentDirections.navigateToUserArticleFragment(it))

                viewModel.onMyArticleNavigated()
            }
        })

        viewModel.liveUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getUser(viewModel.selectedUserEmail)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        if (activity is MainActivity) {
            (activity as MainActivity).recoverToolBarandBottomNav()
        }
        super.onDestroyView()
    }

    private fun setupLayout(user: User) {
        val chipTextSize = 12F
        val chipGroupTalented = binding.chipGroupTalentedSubjects
        val genres = user.talentedSubjects

        for (genre in genres) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chip.setTextAppearanceResource(R.style.WhiteBoldText)
            chip.setChipBackgroundColorResource(R.color.primaryDark)
            chipGroupTalented.addView(chip)
        }

        val chipGroupInterested = binding.chipGroupInterestedSubject
        val interestedList = user.interestedSubjects

        for (genre in interestedList) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chip.setTextAppearanceResource(R.style.WhiteBoldText)
            chip.setChipBackgroundColorResource(R.color.primaryDark)
            chipGroupInterested.addView(chip)
        }

        binding.buttonMessage.setOnClickListener {
            viewModel.postChatRoom(viewModel.createChatRoom())

            if (!user.email.isNullOrEmpty()){
                    Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(NavigationDirections.navigateToMessageFragment(user.email, user.name))
                }, 3000)
                }
        }
    }

    private fun showFollowButton(showFollow: Boolean) {
        // show -> true, show buttonFollow
        // show -> false, show buttonUnFollow

        if (showFollow) {
            binding.buttonFollow.visibility = View.VISIBLE
            binding.buttonUnfollow.visibility = View.GONE
        } else {
            binding.buttonFollow.visibility = View.GONE
            binding.buttonUnfollow.visibility = View.VISIBLE
        }
    }
}