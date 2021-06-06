package com.wenbin.knowhowbinding.freshuser

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.text.bold
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.DialogFreshUserBinding
import com.wenbin.knowhowbinding.login.UserManager

class FreshUserDialogFragment: AppCompatDialogFragment() {

    private lateinit var binding : DialogFreshUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogFreshUserBinding.inflate(inflater)
        Log.d("checkImage", "user = ${UserManager.user}")
        Log.d("checkImage", "image = ${UserManager.user.image}")
        val reminder = SpannableStringBuilder()
            .bold { append(getString(R.string.reminder_new_member_bold)) }
            .append(getString(R.string.reminder_new_member))

        binding.layoutDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_fade_in))
        binding.textContent.text = reminder

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.buttonLater.setOnClickListener {
            dismiss()
        }
        binding.buttonGo.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToEditProfile())
        }

        return binding.root
    }
}