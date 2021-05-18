package com.wenbin.knowhowbinding.calendar.createevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.databinding.DialogCreateEventBinding

class CreateEventDialogFragment : DialogFragment() {
    private lateinit var binding : DialogCreateEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCreateEventBinding.inflate(inflater)

        binding.viewBtnSend.setOnClickListener {
            findNavController().navigate(CreateEventDialogFragmentDirections.navigateToCalendarFragment())
        }
        return binding.root
    }
}