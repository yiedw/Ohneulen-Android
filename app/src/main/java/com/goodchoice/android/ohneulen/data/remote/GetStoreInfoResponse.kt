package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.model.StoreDetail

@Keep
data class GetStoreInfoResponse (
    val resultCode: String,
    val resultData: StoreDetail,
    val resultMsg: String
)