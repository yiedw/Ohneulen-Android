package com.goodchoice.android.ohneulen

import android.app.Application
import android.content.res.Resources
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.goodchoice.android.ohneulen.di.networkModule
import com.goodchoice.android.ohneulen.di.initModule
import com.goodchoice.android.ohneulen.di.viewModelModule
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.fcmToken
import com.goodchoice.android.ohneulen.util.getAppKeyHash
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.lang.Exception
import kotlin.system.exitProcess

class App : Application() {

    companion object {
        lateinit var resources: Resources
        var categorySwitch = MutableLiveData<Int>(0)
        var cookie = HashSet<String>()
    }

    override fun onCreate() {
        super.onCreate()
        val crashlytics= FirebaseCrashlytics.getInstance()



        //initialize
        Companion.resources = resources

        //Timber 초기화
        Timber.plant(Timber.DebugTree())

        //kakao
        getAppKeyHash(this)

        //koin start
        startKoin {
            androidContext(this@App)
            modules(networkModule(BaseUrl.Ohneulen))
            modules(viewModelModule)
            modules(initModule)
        }

        //fcm Token
        fcmToken(applicationContext)
    }

}