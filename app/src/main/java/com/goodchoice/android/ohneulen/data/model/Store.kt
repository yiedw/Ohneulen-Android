package com.goodchoice.android.ohneulen.data.model

import javax.sql.DataSource

data class Store(
    val seq: String,
    val kind: String,
    val cate1: String,
    val cate2: String,
    val cate3: String,
    val brand_seq: Any,
    val storeName: String,
    val bizLicense: String,
    val bizTel: String,
    val openDate: String,
    val contents: String,
    val addrDepth1: String,
    val addrDepth2: String,
    val addrDepth3: String,
    val addrRoadName: String,
    val addrRoad1: String,
    val addrRoad2: String,
    val addrX: String,
    val addrY: String,
    val memo: Any,
    val status: String,
    val insertDate: String,
    val insertID: String,
    val modifyDate: String,
    val modifyID: String,
    val cate1Name: Cate1Name? = null,
    val likes: Boolean,
    val image: List<Image>,
    val photoURL: String,
    val openTime: OpenTime
)

data class Cate1Name(
    val majorName: String,
    val minorName: String
)

//fun getStore(): MutableList<Store> {
//    val samplePartnerList = mutableListOf<Store>()
////    for (i in 0..100) {
////        samplePartnerList.add(
////            Store(
////                i.toString(), "한식",
////                "백반",
////                "한식$i",
////                "백반집$i",
////                "사업자번호",
////                "사업장 전화",
////                "주소", "도로명", 37.499417, 127.0250764, "open", "설명 쌸라쌸라",
////                "2010.04.30",
////                "2020.06.30"
////            )
////        )
////    }
//    return samplePartnerList
//}