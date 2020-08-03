package com.goodchoice.android.ohneulen.data.model

data class StoreDetail(
    val storeInfo: StoreInfo,
    val optionList: List<Option>,
    val storeTime: StoreTime,
    val menuList: List<Any>,
    val hashTagList: List<Keyword>,
    val keywordList: List<Keyword>,
    val reviewCnt: Int,
    val reviewList: List<Review>
)