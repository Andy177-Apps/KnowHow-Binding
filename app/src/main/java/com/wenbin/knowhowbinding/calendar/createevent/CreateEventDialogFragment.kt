package com.wenbin.knowhowbinding.calendar.createevent

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.DialogCreateEventBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.TimeUtil
import kotlinx.android.synthetic.main.dialog_create_event.*
import java.util.*
import com.wenbin.knowhowbinding.util.Logger



class CreateEventDialogFragment : DialogFragment() {
    private val timePickerTypeStart = 0X01
    private val timePickerTypeEnd = 0x02

    private lateinit var binding : DialogCreateEventBinding

    private val viewModel by viewModels<CreateEventViewModel> { getVmFactory(
        CreateEventDialogFragmentArgs.fromBundle(requireArguments()).selectedDate
    ) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCreateEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Setup Time Picker - Start Time
        binding.textViewStartTime.setOnClickListener {
            showTimePickerDialog(timePickerTypeStart, hour, minute)
        }
        // Setup Time Picker - End Time
        binding.textViewEndTime.setOnClickListener {
            showTimePickerDialog(timePickerTypeEnd, hour, minute)
        }
        binding.viewBtnSend.setOnClickListener {
            viewModel.title.value = editText_title.text.toString()
            viewModel.post()
            findNavController().navigate(CreateEventDialogFragmentDirections.navigateToCalendarFragment())
        }

//        binding.spinnerCategory.adapter = CreateEventTypeSpinnerAdapter(
//            KnowHowBindingApplication.instance.resources.getStringArray(R.array.category_array))

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
                    val selectedType = parent.selectedItem.toString()
                    viewModel.setType(selectedType)
                }
            }
        }

//        binding.viewBtnSend.setOnClickListener {
//            if ()
//        }

        viewModel.title.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "it = $it")
        })
        return binding.root
    }

    private fun showTimePickerDialog(type: Int, startHour: Int, startMinute: Int) {
        TimePickerDialog(activity, { _, hour, minute ->
            when (type) {
                timePickerTypeStart -> {
                    binding.textViewStartTime.text = "$hour : $minute"
                    Logger.i("$hour : $minute")
                    viewModel.setStartIme(TimeUtil.timeToStamp("$hour:$minute", Locale.TAIWAN))
                }
                timePickerTypeEnd -> {
                    val timeTimeStamp = TimeUtil.timeToStamp("$hour:$minute", Locale.TAIWAN)
                    viewModel.startTime.value?.let {
                        if (timeTimeStamp > it) {
                            binding.textViewEndTime.text = "$hour : $minute"
                            Logger.i("$hour : $minute")
                            viewModel.setEndTime(timeTimeStamp)
                        } else {
                            Toast.makeText(KnowHowBindingApplication.appContext,
                            getString(R.string.reminder_invalid_time), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }, startHour, startMinute,true).show()
    }
}