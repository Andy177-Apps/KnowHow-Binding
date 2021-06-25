package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainViewModel
import com.wenbin.knowhowbinding.calendar.CalendarViewModel
import com.wenbin.knowhowbinding.chatroom.ChatRoomViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.followedby.FollowedByViewModel
import com.wenbin.knowhowbinding.following.FollowingViewModel
import com.wenbin.knowhowbinding.home.ArticleViewModel
import com.wenbin.knowhowbinding.home.HomeViewModel
import com.wenbin.knowhowbinding.login.LoginViewModel
import com.wenbin.knowhowbinding.myarticle.MyArticleViewModel
import com.wenbin.knowhowbinding.mycollect.MyCollectViewModel
import com.wenbin.knowhowbinding.notify.NotifyViewModel
import com.wenbin.knowhowbinding.postarticle.PostArticleViewModel
import com.wenbin.knowhowbinding.profile.ProfileViewModel
import com.wenbin.knowhowbinding.profile.editprofile.EditProfileViewModel
import com.wenbin.knowhowbinding.search.SearchViewModel

@Suppress("UNCHECKED_CAST")
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

                isAssignableFrom(EditProfileViewModel::class.java) ->
                    EditProfileViewModel(knowHowBindingRepository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(knowHowBindingRepository)

                isAssignableFrom(NotifyViewModel::class.java) ->
                    NotifyViewModel(knowHowBindingRepository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(knowHowBindingRepository)

                isAssignableFrom(ArticleViewModel::class.java) ->
                    ArticleViewModel(knowHowBindingRepository)

                isAssignableFrom(MyArticleViewModel::class.java) ->
                    MyArticleViewModel(knowHowBindingRepository)

                isAssignableFrom(MyCollectViewModel::class.java) ->
                    MyCollectViewModel(knowHowBindingRepository)

                isAssignableFrom(FollowingViewModel::class.java) ->
                    FollowingViewModel(knowHowBindingRepository)

                isAssignableFrom(FollowedByViewModel::class.java) ->
                    FollowedByViewModel(knowHowBindingRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}