package com.goodchoice.android.ohneulen.data.remote

import androidx.annotation.Keep
import com.goodchoice.android.ohneulen.data.model.ImageUpload
import com.goodchoice.android.ohneulen.data.model.Inquire

@Keep
data class GetImageUploadResponse (
    val resultCode:String,
    val resultData:ImageUpload,
    var resultMsg:String
)