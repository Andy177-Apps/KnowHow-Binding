package com.wenbin.knowhowbinding

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.Toolbar

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.wenbin.knowhowbinding.databinding.ActivityMainBinding
import com.wenbin.knowhowbinding.databinding.AppBarMainBinding
import com.wenbin.knowhowbinding.databinding.ContentMainBinding
import com.wenbin.knowhowbinding.databinding.NavHeaderMain02Binding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.CurrentFragmentType
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration



    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_article -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToArticleFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCalendarFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToSearchFragment())
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

//    //Setup drawer navigation destination
//    private val onDrawerItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.myArticleFragment -> {
//                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToMyArticleFragment())
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.myCollectFragment -> {
//                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToMyCollectFragment())
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.editProfileFragment -> {
//                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToEditProfile())
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.searchFragment-> {
//                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToSearchFragment())
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


//        binding.navView.setNavigationItemSelectedListener(onDrawerItemSelectedListener)

        // Test crash for Crashlytics
//        val crashButton = Button(this)
//        crashButton.text = "Crash!"
//        crashButton.setOnClickListener {
//            throw RuntimeException("Test Crash") // Force a crash
//        }
//
//        addContentView(crashButton, ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT))

        // Set up header of drawer ui using data binding
        val bindingNavHeader = NavHeaderMain02Binding.inflate(
            LayoutInflater.from(this), binding.navView, false
        )

        bindingNavHeader.lifecycleOwner = this
        bindingNavHeader.viewModel = viewModel
        binding.navView.addHeaderView(bindingNavHeader.root)

        // Drawer and ToolBar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.myNavHostFragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.articleFragment, R.id.myArticleFragment, R.id.myCollectFragment, R.id.followedByFragment, R.id.followingFragment), drawerLayout)
        //下面這是讓你有左上角那三槓的
        setupActionBarWithNavController(navController, appBarConfiguration)
        //下面這個是讓你可以導航到 Drawer 的其他頁面去的
        navView.setupWithNavController(navController)

        // Drawer and ToolBar End

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

//        viewModel.setUpUser(UserManager.user)

        viewModel.userInfo.observe(this, androidx.lifecycle.Observer {
            Log.d("checkHeader", "userInfo = $it")
        })
        setupBottomNav()
        setupNavController ()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // 這是設置右上角 ToolBar 裡面的 icon
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        viewModel.currentFragmentType.observe(this, androidx.lifecycle.Observer { type ->
            type?.let {
                val profileModeMenu = menu.findItem(R.id.item_profile_mode)
                profileModeMenu.isVisible = when (it) {
                    CurrentFragmentType.PROFILE -> {
//                        profileModeMenu.title = getString(R.string.menu_item_title_edit)
                        false
                    }
                    CurrentFragmentType.EDITPROFILE -> {
//                        profileModeMenu.title = getString(R.string.menu_item_title_save)
                        false
                    }
                    else -> false
                }
                viewModel.saveIsPressed.value = profileModeMenu.isVisible
            }
        })
        return super.onCreateOptionsMenu(menu)
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
        bottomNavView_content.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

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
        textView_ToolBar_title.text = title
    }

    fun coverBottomNav() {
        binding.bottomNavView.visibility = View.GONE
        Log.d("checkfunction", "fun coverBottomNav is used.")
        bottomNavView_content.visibility = View.GONE
    }

    fun hideToolBar() {
        binding.myToolbar.visibility = View.GONE
        toolbar.visibility = View.GONE
    }

    fun recoverToolBarandBottomNav() {
        binding.bottomNavView.visibility = View.VISIBLE
        binding.myToolbar.visibility = View.VISIBLE
        bottomNavView_content.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE

    }


}