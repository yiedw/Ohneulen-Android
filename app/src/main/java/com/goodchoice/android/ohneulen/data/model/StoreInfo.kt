package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StoreInfo(
    val image: List<Image>,
    @SerializedName("store")
    val storeFull: StoreFull
)