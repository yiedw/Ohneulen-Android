package com.goodchoice.android.ohneulen

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.goodchoice.android.ohneulen.di.networkModule
import com.goodchoice.android.ohneulen.di.initModule
import com.goodchoice.android.ohneulen.di.viewModelModule
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.fcmToken
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var resources: Resources
        var cookie = HashSet<String>()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance()



        //initialize
        Companion.resources = resources

        //Timber 초기화
        Timber.plant(Timber.DebugTree())
        //koin start
        startKoin {
            androidContext(this@App)
            modules(networkModule(BaseUrl.OHNEULEN))
            modules(initModule)
            modules(viewModelModule)
        }

        //fcm Token
        fcmToken(applicationContext)
    }

}