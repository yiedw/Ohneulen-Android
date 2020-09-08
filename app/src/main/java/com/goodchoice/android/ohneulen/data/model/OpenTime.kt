package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class OpenTime(
    val codeName:String,
    val starttime:String,
    val endtime:String
)