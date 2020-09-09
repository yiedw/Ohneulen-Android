package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.SearchStore

@Keep
data class SearchStoreResponse (
    val resultCode: String,
    val resultData: List<SearchStore>,
    val resultMsg: String
)