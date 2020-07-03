package com.goodchoice.android.ohneulen.data.remote

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @SerializedName("articles")
    val news: List<News>
)

data class News(
    val title: String,
    @SerializedName("urlToImage")
    val image: String?
)