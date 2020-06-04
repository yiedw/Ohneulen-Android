package com.goodchoice.android.ohneulen

import android.app.Application
import com.goodchoice.android.ohneulen.util.getAppKeyHash
import timber.log.Timber

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        //Timber 초기화
        Timber.plant(Timber.DebugTree())

        //kakao
        getAppKeyHash(this)
    }
}