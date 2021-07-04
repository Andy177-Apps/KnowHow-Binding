package com.wenbin.knowhowbinding.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentUserDetailBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import java.lang.ref.WeakReference

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
            Logger.d("Check_follow, accepted userInfo = $it")

            imageViewGender.isSelected = it.gender == "male"

            Logger.d("Check_follow, viewModel.userInfo.image = ${viewModel.userInfo.value!!.image}")
            if (it.email != "") {
                Logger.d("Check_follow, accepted userInfo in first if determine = $it")

                if (firstTimeEntry) {
                    Logger.d("Check_follow, accepted userInfo in second if determine = $it")
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

        val str = WeakReference(viewModel.userInfo)

        // follow
        viewModel.myInfo.observe(viewLifecycleOwner, Observer { my ->

            Logger.d("Check_follow, myInfo = $my")
            if (my.followingEmail.contains(viewModel.selectedUserEmail)) {

                showFollowButton(false)
            } else {
                showFollowButton(true)
            }
        })

        binding.buttonUnfollow.setOnClickListener {

            Logger.d("Check_follow, Line136")
            Logger.d("Check_follow, Line138")

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

        str.isEnqueued.let {
            Log.d("GC", "str.get() = ${str.get()!!.value}")
        }
        if (str.get()==null) {
            println("str 已經被清除了 ");
            Log.d("GC", "str 已經被清除了 ")

        } else {
            println("str 尚未被清除")
            Log.d("GC", "str 尚未被清除")
        }

        viewModel.liveUser.observe(viewLifecycleOwner, Observer {
            Log.d("GC", "str.get() = ${str.get()!!.value}")
            Logger.d("viewModel.liveArticles.observe, it=$it")
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
            Logger.d("Check_follow, buttonMessage is clicked")

            Logger.d("Check_follow, user.email= ${user.email}")

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
            Logger.d("Check_follow, Line121")

            binding.buttonFollow.visibility = View.VISIBLE
            binding.buttonUnfollow.visibility = View.GONE
        } else {
            Logger.d("Check_follow, Line126")

            binding.buttonFollow.visibility = View.GONE
            binding.buttonUnfollow.visibility = View.VISIBLE
        }
    }
}