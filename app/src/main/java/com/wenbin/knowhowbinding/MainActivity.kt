package com.wenbin.knowhowbinding

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wenbin.knowhowbinding.data.KnowHowBindingRepository
import com.wenbin.knowhowbinding.databinding.ActivityMainBinding
import com.wenbin.knowhowbinding.factory.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
//    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToSearchFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCalendarFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chatroom -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToChatroomFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToProfileFragment())

//                when (viewModel.isLoggedIn) {
//                    true -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToProfileFragment(viewModel.user.value))
//                    }
//                    false -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
//                        return@OnNavigationItemSelectedListener false
//                    }
//                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
//        binding.viewModel = viewModel

        setupBottomNav()
//        setupNavController ()
    }
    /**
     * Set up [BottomNavigationView]
     */
    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

//        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
//        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
//        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
//        bindingBadge.lifecycleOwner = this
//        bindingBadge.viewModel = viewModel
    }

//    private fun setupNavController() {
//        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
//            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
//                R.id.homeFragment -> CurrentFragmentType.HOME
//                R.id.searchFragment -> CurrentFragmentType.SEARCH
//                R.id.calendarFragment-> CurrentFragmentType.CALENDAR
//                R.id.chatRoomFragment -> CurrentFragmentType.CHATROOM
//                R.id.profileFragment -> CurrentFragmentType.PROFILE
//                else -> viewModel.currentFragmentType.value
//            }
//        }
//    }

    fun resetToolBar(title: String){
        binding.textViewToolBarTitle.text = title
    }

}