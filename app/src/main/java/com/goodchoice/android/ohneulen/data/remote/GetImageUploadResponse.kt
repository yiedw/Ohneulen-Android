package com.goodchoice.android.ohneulen.data.remote

import com.goodchoice.android.ohneulen.data.model.ImageUpload
import com.goodchoice.android.ohneulen.data.model.Inquire

data class GetImageUploadResponse (
    val resultCode:String,
    val resultData:ImageUpload,
    var resultMsg:String
)