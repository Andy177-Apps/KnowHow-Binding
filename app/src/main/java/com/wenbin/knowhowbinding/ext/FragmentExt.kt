package com.wenbin.knowhowbinding.ext

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.factory.*
import com.wenbin.knowhowbinding.user.article.UserArticleViewModel
import com.wenbin.knowhowbinding.util.REQUEST_EXTERNAL_STORAGE

/**
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return ViewModelFactory(repository)
}
//fun Fragment.getVmFactory(author: User?): AuthorViewModelFactory {
//    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
//    return AuthorViewModelFactory(repository, author)
//}

fun Fragment.getVmFactory(userEmail: String, userName: String): MessageViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return MessageViewModelFactory(repository, userEmail, userName)
}

fun Fragment.getVmFactory(selectedDate: Long): CreateEventViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return CreateEventViewModelFactory(repository, selectedDate)
}

fun Fragment.getVmFactory(event: Event) : EventViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return EventViewModelFactory(repository, event)
}

fun Fragment.getVmFactory(userEmail: String) : UserProfileViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return UserProfileViewModelFactory(repository, userEmail)
}

fun Fragment.getVmFactory(answer: Answer) : SearchResultViewModelFactory {
    val repository = (requireContext().applicationContext as KnowHowBindingApplication).repository
    return SearchResultViewModelFactory(repository, answer)
}

fun Fragment.checkPermission() {
    val permission = ActivityCompat.checkSelfPermission(KnowHowBindingApplication.instance.applicationContext,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (permission != PackageManager.PERMISSION_GRANTED) {
        //未取得權限，向使用者要求允許權限
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_EXTERNAL_STORAGE
        )
    } else {
        getLocalImg(this)
    }
}

fun Fragment.checkPermission(requestCode : Int) {
    val permission = ActivityCompat.checkSelfPermission(KnowHowBindingApplication.instance.applicationContext,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (permission != PackageManager.PERMISSION_GRANTED) {
        //未取得權限，向使用者要求允許權限
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE
        )
    } else {
        getLocalImg(this, requestCode)
    }
}

fun getLocalImg(fragment: Fragment) {
    ImagePicker.with(fragment)
        .crop()	    			//Crop image(Optional), Check Customization for more option
        .compress(1024)			//Final image size will be less than 1 MB(Optional)
        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
        .start()
}

fun getLocalImg(fragment: Fragment, requestCode : Int) {
    ImagePicker.with(fragment)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start(requestCode)
}

