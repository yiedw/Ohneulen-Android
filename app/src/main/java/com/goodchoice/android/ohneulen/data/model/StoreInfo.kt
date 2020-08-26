package com.goodchoice.android.ohneulen.data.model

import com.google.gson.annotations.SerializedName

data class StoreInfo(
    val image: List<Image>,
    @SerializedName("store")
    val storeFull: StoreFull
)