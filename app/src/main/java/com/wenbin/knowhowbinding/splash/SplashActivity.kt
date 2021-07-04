package com.wenbin.knowhowbinding.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.ActivitySplashBinding
import java.lang.ref.WeakReference

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val duration = 2500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val contextRef = WeakReference(this)

        if (contextRef.get()==null) {
            println("str 已經被清除了 ");
            Log.d("GC", "str 已經被清除了 ")

        } else {
            println("str 尚未被清除")
            Log.d("GC", "str 尚未被清除")
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            if (contextRef.get()==null) {
                println("str 已經被清除了 ");
                Log.d("GC", "str 已經被清除了 ")

            } else {
                println("str 尚未被清除")
                Log.d("GC", "str 尚未被清除")
            }
        }, duration)
        if (contextRef.get()==null) {
            println("str 已經被清除了 ");
            Log.d("GC", "str 已經被清除了 ")

        } else {
            println("str 尚未被清除")
            Log.d("GC", "str 尚未被清除")
        }
    }
}
