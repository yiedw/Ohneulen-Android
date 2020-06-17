package com.goodchoice.android.ohneulen.data.service

import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.remote.KakaoAddressResponse
import com.goodchoice.android.ohneulen.data.remote.KakaoKeywordResponse
import com.goodchoice.android.ohneulen.data.remote.OhneulenResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkService {

    //로그인
    @POST("api/initialize")
    @Multipart
    suspend fun requestLogin(
        @Part("memId") memId: RequestBody,
        @Part("memPw") memPw: RequestBody
    ): OhneulenResponse

    @POST("api/test")
    suspend fun loginTest(): OhneulenResponse

    @POST("api/logout")
    suspend fun logoutTest(): OhneulenResponse

    //필터 항목받아오기
    @POST("api/getcodelist")
    @Multipart
    suspend fun requestCategory(
        @Part("majorCode") majorCode: RequestBody
    ): OhneulenResponse

    //카카오 위치 검색
    @GET("v2/local/search/address.json?")
    fun requestKakaoAddress(
        @Query("query") address: String,
        @Header("Authorization") authorizationKey: String = App.resources.getString(R.string.kakao_rest_key)
    ): KakaoAddressResponse

    @GET("v2/local/search/keyword.json?")
    fun requestKakaoKeyword(
        @Query("query") keyword: String,
        @Header("Authorization") authorizationKey: String = App.resources.getString(R.string.kakao_rest_key)
    ): KakaoKeywordResponse

}