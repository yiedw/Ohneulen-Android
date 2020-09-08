package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class StoreDetail(
    val storeInfo: StoreInfo,
    val optionList: List<Option>,
    val storeTime: StoreTime,
    val menuList: List<StoreMenu>,
    val hashtagList: List<Keyword>,
    val keywordList: List<Keyword>,
    val reviewCnt: Int,
    val reviewList: List<Review>
)