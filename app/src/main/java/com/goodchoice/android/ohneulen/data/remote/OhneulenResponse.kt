package com.goodchoice.android.ohneulen.data.remote

data class OhneulenResponse(
    val resultCode: String,
    val resultData: List<ResultData>,
    val resultMsg: String
)

data class ResultData(
    val etc1: Any,
    val etc2: Any,
    val etc3: Any,
    val etc4: Any,
    val etc5: Any,
    val majorCode: String,
    val minorCode: String,
    val minorName: String,
    val sort: String,
    val useYn: String
)