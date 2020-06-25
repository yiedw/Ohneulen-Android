package com.goodchoice.android.ohneulen.di

import com.goodchoice.android.ohneulen.data.repository.InitData
import org.koin.dsl.module

val initModule =module{
    single{
        InitData(get())
    }
}