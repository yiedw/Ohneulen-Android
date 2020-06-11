package com.goodchoice.android.ohneulen.data.service

import com.goodchoice.android.ohneulen.data.remote.OhneulenResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkService {

    @POST("api/initialize")
    @Multipart
    suspend fun ohneulenLogin(
        @Part("memId") memId: RequestBody,
        @Part("memPw") memPw: RequestBody
    ): OhneulenResponse

    @POST("api/test")
    suspend fun loginTest():OhneulenResponse

    @POST("api/logout")
    suspend fun logoutTest():OhneulenResponse

    @POST("api/getcodelist")
    @Multipart
    suspend fun mainCategory(
        @Part("majorCode") majorCode:RequestBody
    ):OhneulenResponse
}