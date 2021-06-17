package com.wenbin.knowhowbinding.calendar.createevent

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerListener
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch.LimitExceedListener
import com.facebook.FacebookSdk.getApplicationContext
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.DialogCreateEventBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger
import com.wenbin.knowhowbinding.util.TimeUtil
import kotlinx.android.synthetic.main.dialog_create_event.view.*
import java.util.*


class CreateEventDialogFragment : AppCompatDialogFragment() {
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

        // Multiple Spinner for followingName
        val listArray0: MutableList<KeyPairBoolData> = ArrayList()

        viewModel.followingName.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "followingName = $it")
            binding.spinnerOtherUser.adapter = CreateEventFollowingSpinnerAdapter(it)
            it?.let {
                for (i in it.indices) {
                    val h = KeyPairBoolData()
                    h.id = (i + 1).toLong()
                    h.name = it[i]
                    h.isSelected = false
                    listArray0.add(h)
                }
            }
            Log.d("MultipleSpinner", "Updated listArray0 = $listArray0")

        })

        Log.d("MultipleSpinner", "listArray0 in line 67= $listArray0")

        binding.multipleItemSelectionSpinner.isSearchEnabled = true
        binding.multipleItemSelectionSpinner.setSearchHint("搜尋好友")
        binding.multipleItemSelectionSpinner.setClearText("關閉 & 清除")
        binding.multipleItemSelectionSpinner.setEmptyTitle("沒有選擇任何人喔!")
        binding.multipleItemSelectionSpinner.setItems(listArray0, MultiSpinnerListener { items ->
            val list = mutableListOf<String>()

            for (i in items.indices) {
                if (items[i].isSelected) {
                    Log.d(
                        "MultipleSpinner",
                        i.toString() + " : " + items[i].name + " : " + items[i].isSelected
                    )
                    list.add(items[i].name)
                }
            }

            Log.d("MultipleSpinner", "final list in line 83 =$list")
            viewModel.setMultipleInvitation(list)
        })

        viewModel.multipleInvitation.observe(viewLifecycleOwner, Observer {
            Log.d("MultipleSpinner", "multipleInvitation = $it")
        })



        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Setup TimePickDialog visibility associated is all day
        binding.switchIsAllDay.setOnClickListener {
            if (it.switch_is_all_day.isChecked) {
                timeIntervalVisibility(true)
                viewModel.setAllDay(true)
            } else {
                timeIntervalVisibility(false)
                viewModel.setAllDay(false)
            }
        }

        // Set up textView_date to show selected date by safe arg from calendar page.
        binding.textViewDate.text = viewModel.date

        // Setup Time Picker - Start Time
        binding.textViewStartTime.setOnClickListener {
            showTimePickerDialog(timePickerTypeStart, hour, minute)
        }
        // Setup Time Picker - End Time
        binding.textViewEndTime.setOnClickListener {
            showTimePickerDialog(timePickerTypeEnd, hour, minute)
        }
        binding.viewBtnSend.setOnClickListener {
            val event = viewModel.getEvent()
            Log.d("newEvent", "event = $event")
            Logger.i("${viewModel.startTime.value}")

            viewModel.post(event)
            Logger.i("$event")
            findNavController().navigate(CreateEventDialogFragmentDirections.navigateToCalendarFragment())
        }

        binding.spinnerCategory.adapter = CreateEventTypeSpinnerAdapter(
            KnowHowBindingApplication.instance.resources.getStringArray(R.array.category_array)
        )

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
                    Log.d("Spinner_type", "position = $position")
                    Log.d("Spinner_type", "id = $id")

                    viewModel.setType(selectedType)
                }
            }
        }

        viewModel.type.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "createEventViewModel type = $it")
        })

        // Set Invitation
        binding.spinnerOtherUser.onItemSelectedListener = object :
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
                    Log.d("Spinner_Following", "position = $position")
                    Log.d("Spinner_Following", "id = $id")
                    viewModel.setInvitation(position)
                }
            }
        }


        viewModel.userInfo.observe(viewLifecycleOwner, Observer { it ->
            Log.d("wenbin", "got user = $it")

            Log.d("wenbin", "got user following = ${it.following}")

            // Get userEmail list from list of following
            val list = arrayListOf<String>()

            // Give following name
            for (item in it.following) {
                Log.d("wenbin", "userEmail = ${item.userName}")
                list.add(item.userName)
            }
            Log.d("wenbin", "New list = $list")
            viewModel.getFollowingName(list)
        })



        viewModel.invitation.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "createEventViewModel invitation = $it")
        })

        viewModel.title.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "it = $it")
        })
        return binding.root
    }

    private fun timeIntervalVisibility(condition: Boolean) {
        if (condition) {
            binding.textViewStartTime.visibility = View.GONE
            binding.textViewEndTime.visibility = View.GONE
        } else {
            binding.textViewStartTime.visibility = View.VISIBLE
            binding.textViewEndTime.visibility = View.VISIBLE
        }
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
                            Toast.makeText(
                                KnowHowBindingApplication.appContext,
                                getString(R.string.reminder_invalid_time), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }, startHour, startMinute, true).show()
    }
}