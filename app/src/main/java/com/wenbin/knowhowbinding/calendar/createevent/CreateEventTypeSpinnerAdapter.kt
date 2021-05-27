package com.wenbin.knowhowbinding.calendar.createevent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.ItemTypeSpinnerBinding

class CreateEventTypeSpinnerAdapter(private val strings: Array<String>) : BaseAdapter(){
    override fun getCount(): Int {
        return strings.size+1
    }

    override fun getItem(position: Int): Any {
        return if (position == 0) 0
        else strings[position-1]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemTypeSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        if (position == 0) {
            binding.type = "Select Type"
            binding.textSpinnerTitle.setTextColor(KnowHowBindingApplication.appContext.applicationContext.getColor(R.color.black_12_alpha))
        }else {
            binding.type = strings[position-1]
        }
        return binding.root
    }
}