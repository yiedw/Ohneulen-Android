package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class TestResponse(
    @SerializedName("articles")
    val news: List<News>
)
@Keep
data class News(
    val title: String,
    @SerializedName("urlToImage")
    val image: String?
)