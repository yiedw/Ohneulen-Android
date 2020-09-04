package com.goodchoice.android.ohneulen.data.model

data class LikeStore(
    val addrDepth1: String,
    val addrDepth2: String,
    val addrDepth3: String,
    val addrRoad1: String,
    val addrRoad2: String,
    val addrRoadName: String,
    val addrX: String,
    val addrY: String,
    val bizLicense: String,
    val bizTel: String,
    val brand_seq: String,
    val cate1: String,
    val cate1Name: String,
    val cate2: String,
    val cate3: String,
    val contents: String,
    val image: List<LikeStoreImage>,
    val insertDate: String,
    val insertID: String,
    val kind: String,
    val memo: String,
    val modifyDate: String,
    val modifyID: String,
    val openDate: String,
    val seq: String,
    val status: String,
    val storeName: String
)

data class LikeStoreImage(
    val insertDate: String,
    val insertID: String,
    val kind: String,
    val mirror_seq: String,
    val modifyDate: String,
    val modifyID: String,
    val photoURL: String,
    val seq: String,
    val sort: String
)