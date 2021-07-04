package com.wenbin.knowhowbinding.home

import android.app.ActivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentArticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger
import kotlinx.android.synthetic.*
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

        val viewModel by viewModels<ArticleViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val queue = ReferenceQueue<Int?>()
        var mWeakReference: WeakReference<Int?>? = null
        var number: Int? =1

        mWeakReference = WeakReference(number , queue)

        binding.imageViewSearch.setOnClickListener {
            System.gc()

            Log.d("GC", "binding.imageViewSearch is clicked")
            Log.d("GC", "number = $number")

            Log.d("GC", "mWeakReference.get()" + mWeakReference!!.get())


            number = null
            System.gc()
            Log.d("GC", "Updated number = $number")

            Log.d("GC", "mWeakReference.get()" + mWeakReference!!.get())


            Logger.d("checkSearch, binding.imageViewSearch is clicked")

            Logger.d("checkSearch, binding.editTextSearch.text.toString() = ${binding.editTextSearch.text}")

            viewModel.searchEditText.value = binding.editTextSearch.text.toString()
        }

        viewModel.searchEditText.observe(viewLifecycleOwner, Observer {
            Logger.d("checkSearch, searchEditText in ArticleFragment = $it")
        })

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewpagerArticles.let {
                tabsArticles.setupWithViewPager(it)
                it.adapter = ArticleAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsArticles))
            }
            if (activity is MainActivity) {
                (activity as MainActivity).hideToolBar()
                (activity as MainActivity).resetToolBar("")
            }
            return@onCreateView root
        }
    }

    override fun onDestroyView() {
        if (activity is MainActivity) {
            (activity as MainActivity).recoverToolBarandBottomNav()
        }
        super.onDestroyView()
    }

}