package com.wenbin.knowhowbinding.ext

import androidx.fragment.app.Fragment
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.factory.ViewModelFactory

/**
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).knowHowBindingRepository
    return ViewModelFactory(repository)
}