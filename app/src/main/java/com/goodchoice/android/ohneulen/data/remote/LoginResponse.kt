package com.goodchoice.android.ohneulen.data.remote

data class LoginResponse(
    val resultCode: String,
    val resultData: ResultData,
    val resultMsg: String
)

data class ResultData(
    val `data`: List<Data>,
    val resultCode: String,
    val resultMsg: String
)

data class Data(
    val Tocken: String?,
    val birth: String,
    val ci: String,
    val deviceType: String?,
    val domain: String,
    val foreign: String,
    val id: String,
    val insertDate: String,
    val lastLoginD: String,
    val modifyDate: String,
    val name: String,
    val phone: String,
    val phoneType: String,
    val photoURL: String,
    val pwd: String,
    val reason_code: String?,
    val reason_text: String?,
    val seq: String,
    val sex: String,
    val status: String
)