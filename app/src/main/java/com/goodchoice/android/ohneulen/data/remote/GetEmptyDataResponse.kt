package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.Store

@Keep
data class GetEmptyDataResponse(
    val resultCode: String,
    val resultMsg: String
)