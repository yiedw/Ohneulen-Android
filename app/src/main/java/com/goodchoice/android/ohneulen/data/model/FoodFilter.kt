package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class FoodFilter(
    @SerializedName("mainCategory")
    val mainCategory:String,
    @SerializedName("subCategory")
    val subCategory:MutableList<String>
)