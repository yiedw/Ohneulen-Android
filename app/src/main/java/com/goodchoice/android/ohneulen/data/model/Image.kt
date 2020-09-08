package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class Image(
    val insertDate: String,
    val insertID: String,
    val kind: String,
    val mirror_seq: String,
    val modifyDate: String,
    val modifyID: String,
    val photoURL: String,
    val seq: String,
    val sort: String
)