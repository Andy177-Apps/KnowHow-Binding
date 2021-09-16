package com.wenbin.knowhowbinding.home

import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.wenbin.knowhowbinding.data.Article

class MyEntryController : TypedEpoxyController<List<Article>>() {
    override fun buildModels(data: List<Article>?) {
        data?.forEach { it ->
            Log.d("Epoxy","data in TypedEpoxyController = $it")

            myEntry {
                id(hashCode())
                article(it)
            }
        }
    }
}