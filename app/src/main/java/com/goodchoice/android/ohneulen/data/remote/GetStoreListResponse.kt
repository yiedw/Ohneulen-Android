package com.goodchoice.android.ohneulen.data.remote

import com.goodchoice.android.ohneulen.data.model.Store

data class GetStoreListResponse (
    val resultCode: String,
    val resultData: List<Store>,
    val resultMsg: String
)