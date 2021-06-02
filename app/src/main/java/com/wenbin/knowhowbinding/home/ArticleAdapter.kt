package com.wenbin.knowhowbinding.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R

class ArticleAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> HomeFragment()
            1 -> HomeFragment()
            2 -> HomeFragment()
            3 -> HomeFragment()

            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> KnowHowBindingApplication.appContext.getString(R.string.pager_title_all)
            1 -> KnowHowBindingApplication.appContext.getString(R.string.pager_find_student)
            2 -> KnowHowBindingApplication.appContext.getString(R.string.pager_find_tutor)
            3 -> KnowHowBindingApplication.appContext.getString(R.string.pager_skill_exchange)

            else -> KnowHowBindingApplication.appContext.getString(R.string.pager_study_group)
        }
    }
}