package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.Store

@Keep
data class GetStoreListResponse (
    val resultCode: String,
    val resultData: List<Store>,
    val resultMsg: String
)