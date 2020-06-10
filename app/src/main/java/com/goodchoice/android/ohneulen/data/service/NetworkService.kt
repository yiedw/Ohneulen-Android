package com.goodchoice.android.ohneulen.data.service

import com.goodchoice.android.ohneulen.data.remote.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkService {

    @POST("api/initialize")
    suspend fun ohneulenLogin(
    @Body body:RequestBody
    ):LoginResponse
}