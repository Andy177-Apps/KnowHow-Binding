package com.wenbin.knowhowbinding

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable


/**
 * Created by fcn-mq on 2017/5/22.
 * 自定义View
 */
class MyView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Get height of the View
        val width = width
        val height = height
        val colorStart = KnowHowBindingApplication.appContext.applicationContext.getColor(R.color.colorStart)
        val color1 = KnowHowBindingApplication.appContext.applicationContext.getColor(R.color.color1)
        val colorEnd = KnowHowBindingApplication.appContext.applicationContext.getColor(R.color.colorEnd)
        val paint = Paint()
        val backGradient: LinearGradient = LinearGradient(0F, 0F, measuredWidth.toFloat(), 0F, intArrayOf(colorStart, color1, colorEnd), null, Shader.TileMode.CLAMP)
        // LinearGradient backGradient = new LinearGradient(0, 0, 0, height, new int[]{colorStart, color1 ,colorEnd}, null, Shader.TileMode.CLAMP);
        paint.shader = backGradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}