package com.goodchoice.android.ohneulen.data.model

import javax.sql.DataSource

data class Store(
    val seq: String,
    val firstCate: String,
    val secondCate: String,
    val brandSeq: String,
    val storeName: String,
    val bizLicense: String,
    val bizTel: String,
    val addressOld: String,
    val addressRoad: String,
    val addressX: Double,
    val addressY: Double,
    val status: String,
    val contents: String,
    val open: String,
    val update:String

)

fun getStore(): MutableList<Store> {
    val samplePartnerList = mutableListOf<Store>()
    for (i in 0..100) {
        samplePartnerList.add(
            Store(
                i.toString(), "한식",
                "백반",
                "한식$i",
                "백반집$i",
                "사업자번호",
                "사업장 전화",
                "주소", "도로명", 37.499417, 127.0250764, "open", "설명 쌸라쌸라",
                "2010.04.30",
                "2020.06.30"
            )
        )
    }
    return samplePartnerList
}