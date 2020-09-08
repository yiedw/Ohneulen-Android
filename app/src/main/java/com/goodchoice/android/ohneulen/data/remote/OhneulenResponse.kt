package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OhneulenResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultData")
    val resultData: List<ResultData>,
    @SerializedName("resultMsg")
    val resultMsg: String
)

@Keep
data class ResultData(
    @SerializedName("etc1")
    val etc1: Any,
    @SerializedName("etc2")
    val etc2: Any,
    @SerializedName("etc3")
    val etc3: Any,
    @SerializedName("etc4")
    val etc4: Any,
    @SerializedName("etc5")
    val etc5: Any,
    @SerializedName("majorCode")
    val majorCode: String,
    @SerializedName("minorCode")
    val minorCode: String,
    @SerializedName("minorName")
    val minorName: String,
    @SerializedName("sort")
    val sort: String,
    @SerializedName("useYn")
    val useYn: String
)