package com.goodchoice.android.ohneulen.model

data class Store(
    val seq: String,
    val firstCate: String,
    val secondCate: String,
    val brandSeq: String,
    val storeName: String,
    val addressOld: String,
    val addressRoad: String,
    val addressXY: String,
    val status: String,
    val pointTotal: String = "0",
    val point_1: String = "0",
    val point_2: String = "0",
    val point_3: String = "0",
    val point_4: String = "0",
    val point_5: String = "0",
    val point_6: String = "0",
    val point_7: String = "0"
)

fun getStore(): MutableList<Store> {
    val samplePartnerList = mutableListOf<Store>()
    for (i in 0..10) {
        samplePartnerList.add(
            Store(
                i.toString(), "한식",
                "백반",
                "한식$i",
                "백반집$i",
                "주소", "도로명", "3,4", "open"
            )
        )
    }
    return samplePartnerList
}
