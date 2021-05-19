package com.wenbin.knowhowbinding.calendar.createevent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.databinding.DialogCreateEventBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import kotlinx.android.synthetic.main.dialog_create_event.*

class CreateEventDialogFragment : DialogFragment() {
    private lateinit var binding : DialogCreateEventBinding
    val viewModel by viewModels<CreateEventViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCreateEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.viewBtnSend.setOnClickListener {
            viewModel.title.value = editText_title.text.toString()
            viewModel.post()
            findNavController().navigate(CreateEventDialogFragmentDirections.navigateToCalendarFragment())
        }

        viewModel.title.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "it = $it")
        })
        return binding.root
    }
}