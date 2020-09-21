package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.Inquire

@Keep
data class GetBoardResponse (
    val resultCode:String,
    val resultData:List<Inquire>,
    var resultMsg:String
)