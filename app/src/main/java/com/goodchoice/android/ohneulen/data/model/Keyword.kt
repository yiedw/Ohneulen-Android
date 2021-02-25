package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class Keyword(
    val icon: String,
    val keyword: String,
    val keyword_seq: String,
    val kind: String,
    
    val seq: String,
    val store_seq: String
)