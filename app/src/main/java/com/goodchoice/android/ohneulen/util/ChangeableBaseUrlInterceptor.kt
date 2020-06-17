package com.goodchoice.android.ohneulen.util

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class ChangeableBaseUrlInterceptor : Interceptor {

    @Volatile
    private var host: HttpUrl? = null

    fun setHost(host: String) {
        this.host = host.toHttpUrlOrNull()
    }

    fun clear() {
        host = null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request=chain.request()
        host?.let {
            val newUrl=request.url.newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()
            request=request.newBuilder().url(newUrl).build()
        }
        return chain.proceed(request)
    }
}