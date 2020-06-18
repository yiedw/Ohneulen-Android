package com.goodchoice.android.ohneulen.di

import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.home.HomeViewModel
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.partner.PartnerViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { PartnerViewModel() }
    single {
        MainViewModel()
    }
}