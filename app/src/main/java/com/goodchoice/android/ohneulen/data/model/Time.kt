package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class Time (
    val day: String,
    val day_name: String,
    val endMin: String,
    val endTime: String,
    val kind: String,
    val kind_name: String,
    val seq: String,
    val startMin: String,
    val startTime: String,
    val store_seq: String
)