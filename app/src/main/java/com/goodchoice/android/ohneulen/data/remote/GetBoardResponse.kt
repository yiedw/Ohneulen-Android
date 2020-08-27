package com.goodchoice.android.ohneulen.data.remote

import com.goodchoice.android.ohneulen.data.model.Inquire

data class GetInquireResponse (
    val resultCode:String,
    val resultData:List<Inquire>,
    var resultMsg:String
)