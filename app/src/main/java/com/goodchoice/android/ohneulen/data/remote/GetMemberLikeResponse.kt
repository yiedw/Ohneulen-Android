package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.LikeStore

@Keep
data class GetMemberLikeResponse (
    val resultCode:String,
    val resultMsg:String,
    val resultData:List<LikeStore>
)
