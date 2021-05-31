package com.wenbin.knowhowbinding

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.wenbin.knowhowbinding.databinding.ActivityMainBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.CurrentFragmentType
import java.util.*

class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding



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
        binding.viewModel = viewModel

        var db = FirebaseFirestore.getInstance()


        //隨時監聽整份 collectionPath 變化
        db.collection("Friends")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("TAG", "listen:error", e)
                        return@addSnapshotListener
                    }

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> Log.d(
                                    "TAG",
                                    "New Invitation Card: ${dc.document.data}"
                            )
                            DocumentChange.Type.MODIFIED -> {
                                Log.d(
                                        "TAG",
                                        "Changed Data: ${dc.document.data}"
                                )

                            }

                            DocumentChange.Type.REMOVED -> Log.d(
                                    "TAG",
                                    "Removed Invitation Card: ${dc.document.data}"
                            )
//                        inviter = dc.document.data["name"]
                        }
                    }
                }

        setupBottomNav()
        setupNavController ()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_profile_mode -> {
                when (item.title.toString().toLowerCase(Locale.ROOT)) {
                    "save" -> viewModel.saveIsPressed.value = true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Set up [BottomNavigationView]
     */
    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.searchFragment -> CurrentFragmentType.SEARCH
                R.id.calendarFragment-> CurrentFragmentType.CALENDAR
                R.id.chatRoomFragment -> CurrentFragmentType.CHATROOM
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    fun resetToolBar(title: String){
        binding.textViewToolBarTitle.text = title
        binding.bottomNavView.visibility = View.VISIBLE
    }

    fun coverBottomNav() {
        binding.bottomNavView.visibility = View.GONE
    }

    fun hideToolBar() {
        binding.myToolbar.visibility = View.GONE
    }


}