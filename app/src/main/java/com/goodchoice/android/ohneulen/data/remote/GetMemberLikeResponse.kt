package com.goodchoice.android.ohneulen.data.remote

import com.goodchoice.android.ohneulen.data.model.LikeStore

data class GetMemberLikeResponse (
    val resultCode:String,
    val resultMsg:String,
    val resultData:List<LikeStore>
)
