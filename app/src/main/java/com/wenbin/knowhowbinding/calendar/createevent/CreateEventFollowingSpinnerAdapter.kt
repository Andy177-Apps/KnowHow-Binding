package com.wenbin.knowhowbinding.calendar.createevent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.ItemUserFollowdSpinnerBinding

class CreateEventFollowingSpinnerAdapter(private val strings: ArrayList<String>): BaseAdapter() {
    override fun getCount(): Int {
        return strings.size + 1
    }

    override fun getItem(position: Int): Any {
        return if (position == 0)
            0
        else
            strings[position - 1]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemUserFollowdSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

        if (position == 0) {
            binding.following = KnowHowBindingApplication.instance.resources.getString(R.string.spinner_select_partner)
            binding.textSpinnerTitle.setTextColor(KnowHowBindingApplication.appContext.applicationContext.getColor(R.color.black_12_alpha))

        } else {
            binding.following = strings[position - 1]
        }

        return binding.root
    }
}