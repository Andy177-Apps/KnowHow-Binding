package com.wenbin.knowhowbinding.postarticle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.calendar.createevent.CreateEventTypeSpinnerAdapter
import com.wenbin.knowhowbinding.databinding.FragmentPostarticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger

class PostArticleFragment : Fragment(){
    private lateinit var binding: FragmentPostarticleBinding
    val viewModel by viewModels<PostArticleViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostarticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Logger.d("check_article, userInfo = $it")
        })

        binding.buttonSendArticle.setOnClickListener {
            if (viewModel.isFormFilled()) {
                val article = viewModel.getArticle()
                viewModel.publish(article)
                findNavController().navigate(PostArticleFragmentDirections.navigateToArticleFragment())
            }
            else {
                Toast.makeText(this.context, "地點, 提供, 想找和 Type 都要選擇喔！", Toast.LENGTH_SHORT).show()
            }
        }

        binding.spinnerCategory.adapter = PostArticleTypeSpinnerAdapter(
                KnowHowBindingApplication.instance.resources.getStringArray(R.array.category_array))

        binding.spinnerCategory.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                if (parent != null) {
                    viewModel.articleType.value = parent.selectedItem.toString()
                }
            }
        }

        viewModel.articleType.observe(viewLifecycleOwner, Observer {
            Log.d("checkFormFilled", "articleType = $it")
        })

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("發文")
            (activity as MainActivity).coverBottomNav()
        }
        return binding.root
    }
}