package com.goodchoice.android.ohneulen.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.goodchoice.android.ohneulen.App
import okhttp3.Interceptor
import okhttp3.Response

class ReceivedCookiesInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = HashSet<String>()

            for (header: String in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            App.cookie =cookies
        }
        return originalResponse
    }
}