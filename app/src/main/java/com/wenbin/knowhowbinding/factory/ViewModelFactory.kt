package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainViewModel
import com.wenbin.knowhowbinding.chatroom.ChatRoomViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.home.HomeViewModel
import com.wenbin.knowhowbinding.postarticle.PostArticleViewModel

class ViewModelFactory constructor(
    private val knowHowBindingRepository: KnowHowBindingRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(knowHowBindingRepository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(knowHowBindingRepository)

                isAssignableFrom(PostArticleViewModel::class.java) ->
                    PostArticleViewModel(knowHowBindingRepository)

                isAssignableFrom(ChatRoomViewModel::class.java) ->
                    ChatRoomViewModel(knowHowBindingRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}