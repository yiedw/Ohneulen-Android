package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchStore(
    var P_1: Double,
    val P_2: Double,
    val P_3: Double,
    val P_4: Double,
    val P_5: Double,
    val P_6: Double,
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
    @SerializedName("brand_seq")
    val brandSeq: String,
    val cate1: String,
    val cate2: String,
    val cate3: String,
    val contents: String,
    val insertDate: String,
    val insertID: String,
    val kind: String,
    var like: Boolean=false,
    @SerializedName("like_cnt")
    var likeCnt: Int,
    val memo: String,
    val modifyDate: String,
    val modifyID: String,
    val openDate: String,
    @SerializedName("opentime")
    val openTime: String,
    val photoURL: String,
    @SerializedName("review_cnt")
    var reviewCnt: Int,
    val seq: String,
    val status: String,
    val storeName: String
)