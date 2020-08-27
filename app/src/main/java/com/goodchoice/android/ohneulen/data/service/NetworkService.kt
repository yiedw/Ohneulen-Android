package com.goodchoice.android.ohneulen.data.service

import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.remote.*
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
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
    suspend fun requestLoginTest(): OhneulenResponse

    @POST("api/logout")
    suspend fun requestLogoutTest(): OhneulenResponse

    //필터 항목 받아오기
    @POST("api/getcodelist")
    @Multipart
    suspend fun requestOhneulenData(
        @Part("majorCode") majorCode: RequestBody
    ): OhneulenResponse


    //스토어 디테일 받아오기
    @POST("api/getstoreinfo")
    @Multipart
    suspend fun requestGetStoreInfo(
        @Part("store_seq") storeSeq: RequestBody
    ): GetStoreInfoResponse

    //스토어 리스트 검색
    //    @Multipart
    @POST("store/searchList")
    @FormUrlEncoded
    suspend fun requestStoreSearchList(
        @Field("addry[]") addry: List<Double>,
        @Field("addrx[]") addrx: List<Double>,
        @Field("cate[]") cate: List<String>,
        @Field("option[]") option: List<String>,
        @Field("openTime[]") openTime: List<String>,
        @Field("sort[]") sort: List<String>
    ): GetStoreListResponse

    //찜 설정
    @POST("api/set_memberLike")
    @FormUrlEncoded
    suspend fun requestSetMemberLike(
        @Field("store_seq") storeSeq: String
    ): GetEmptyDataResponse

    //찜 목록 가져오기
    @POST("api/get_memberLike")
    suspend fun requestGetMemberLike(
    ): GetStoreListResponse

    //문의 내역 가져오기
    @POST("api/get_board")
    suspend fun requestGetInquire(
    ):GetInquireResponse

    //문의 내역 보내기
    @POST("api/set_board")
    @FormUrlEncoded
    suspend fun requestSetInquire(
        @Field("gubun1") gubun1:String,
        @Field("title") title:String,
        @Field("contents") contents:String
    ):GetInquireResponse


    //카카오
    //카카오 위치 검색
    @GET
    suspend fun requestKakaoAddress(
        @Url url: String = BaseUrl.KAKAO_MAP + "v2/local/search/address.json?",
        @Query("query") address: String,
        @Header("Authorization") authorizationKey: String = App.resources.getString(R.string.kakao_rest_key)
    ): KakaoAddressResponse

    @GET
    suspend fun requestKakaoKeyword(
        @Url url: String = BaseUrl.KAKAO_MAP + "v2/local/search/keyword.json?",
        @Query("query") keyword: String,
        @Header("Authorization") authorizationKey: String = App.resources.getString(R.string.kakao_rest_key)
    ): KakaoKeywordResponse


}