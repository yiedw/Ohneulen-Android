package com.goodchoice.android.ohneulen.data.model

data class StoreTime(
    val close: List<Time>,
    val open: List<Time>,
    val breakTime:List<Time>
)