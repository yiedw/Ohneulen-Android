package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import javax.sql.DataSource

@Keep
data class StoreFull (
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
    val cate1Name: Cate1Name?=null,
    val likes: Boolean,
    val image: List<Image>,
    val photoURL: String,
    val openTime: List<OpenTime>,
    val opentime:String
)

@Keep
data class Cate1Name(
    val majorName: String?,
    val minorName: String?
)
