package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainViewModel
import com.wenbin.knowhowbinding.calendar.CalendarViewModel
import com.wenbin.knowhowbinding.calendar.createevent.CreateEventViewModel
import com.wenbin.knowhowbinding.chatroom.ChatRoomViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.home.HomeViewModel
import com.wenbin.knowhowbinding.postarticle.PostArticleViewModel
import com.wenbin.knowhowbinding.search.SearchViewModel

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

                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(knowHowBindingRepository)

                isAssignableFrom(CalendarViewModel::class.java) ->
                    CalendarViewModel(knowHowBindingRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}