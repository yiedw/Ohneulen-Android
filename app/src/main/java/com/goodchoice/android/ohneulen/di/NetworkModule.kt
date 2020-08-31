package com.goodchoice.android.ohneulen.di

import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.util.AddCookiesInterceptor
import com.goodchoice.android.ohneulen.util.ReceivedCookiesInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CONNECT_TIMEOUT = 10L
private const val WRITE_TIMEOUT = 1L
private const val READ_TIMEOUT = 20L


fun networkModule(baseUrl: String) = module {

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(AddCookiesInterceptor())
            addInterceptor(ReceivedCookiesInterceptor())
//            addInterceptor(ChangeableBaseUrlInterceptor())
        }.build()
    }

    single {
        val gson=GsonBuilder().setLenient().create()
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NetworkService::class.java)
    }


}