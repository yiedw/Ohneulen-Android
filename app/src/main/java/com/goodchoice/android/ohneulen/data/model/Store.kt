package com.goodchoice.android.ohneulen.data.model

import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import javax.sql.DataSource

data class Store (
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
    val cate1Name: String,
    val likes: Boolean,
    val image: List<Image>,
    val photoURL: String,
    val openTime: List<OpenTime>,
    val opentime:String
)

