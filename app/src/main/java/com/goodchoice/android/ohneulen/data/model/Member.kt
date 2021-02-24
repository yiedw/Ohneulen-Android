package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class Member (
    val id:String,
    val name:String,
    val nickName:String,
    val phoneNum:String
    
)


fun getMember():Member{
    return Member("ID","이름","닉네임","폰넘버")
}