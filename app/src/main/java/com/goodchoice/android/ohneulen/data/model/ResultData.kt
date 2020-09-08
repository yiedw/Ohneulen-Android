package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class ResultData(
    val hashtagList: List<Any>,
    val keywordList: List<Keyword>,
    val menuList: List<Any>,
    val optionList: List<Option>,
    val reviewCnt: Int,
    val reviewList: List<Review>,
    val storeInfo: StoreDetail,
    val storeTime: StoreTime
)