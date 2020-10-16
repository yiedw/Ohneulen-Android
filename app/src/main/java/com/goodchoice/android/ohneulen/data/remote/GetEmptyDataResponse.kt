package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.Store

@Keep
data class GetEmptyDataResponse(
    var resultCode: String,
    val resultMsg: String
)