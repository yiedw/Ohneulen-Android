package com.goodchoice.android.ohneulen.di

import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.home.HomeViewModel
import com.goodchoice.android.ohneulen.ui.like.LikeViewModel
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.mypage.MyPageViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

val viewModelModule = module {
    viewModel { HomeViewModel() }
    single { StoreViewModel(get()) }
    viewModel { MyPageViewModel() }
    single { LoginViewModel(get(), androidApplication()) }
    single { SearchViewModel(get(), get()) }
    single {
        MainViewModel()
    }
    viewModel { LikeViewModel(get()) }
}