package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.MemberInfo

@Keep
data class GetMemberInfoResponse (
    val resultCode:String,
    val resultData: MemberInfo,
    var resultMsg:String
)