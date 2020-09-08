package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GetStoreInfo(
    @SerializedName("hashtagList")
    val hashTagList: List<GetStoreInfoHashTag>,
    val keywordList: List<GetStoreInfoKeyword>,
    val menuList: List<GetStoreInfoMenu>,
    val optionList: List<GetStoreInfoOption>,
    val reviewCnt: Int,
    val reviewList: List<GetStoreInfoReview>,
    val storeInfo: GetStoreInfoImageStore,
    val storeTime: GetStoreInfoTime
)

data class GetStoreInfoHashTag(
    val icon: Any,
    val keyword: String,
    val keyword_seq: String,
    val kind: String,
    val seq: String,
    val store_seq: String
)

data class GetStoreInfoKeyword(
    val icon: String,
    val keyword: String,
    val keyword_seq: String,
    val kind: String,
    val seq: String,
    val store_seq: String
)

data class GetStoreInfoMenu(
    val contents: String,
    val insertDate: String,
    val insertID: String,
    val modifyDate: String,
    val modifyID: String,
    val photoURL: String,
    val price: String,
    val seq: String,
    val sort: String,
    val status: String,
    val store_seq: String,
    val title: String
)

data class GetStoreInfoOption(
    val insertDate: String,
    val insertID: String,
    val kind: String,
    val modifyDate: String,
    val modifyID: String,
    val optionKind: String,
    val optionText: String,
    val option_img: String,
    val option_kind_name: String,
    val option_name: String,
    val seq: String,
    val store_seq: String
)

data class GetStoreInfoReview(
    val content: String,
    val imgList: List<GetStoreInfoImage>,
    val insertDate: String,
    val member_seq: String,
    val modifyDate: String,
    val point_1: String,
    val point_2: String,
    val point_3: String,
    val point_4: String,
    val point_5: String,
    val point_6: String,
    val point_7: String,
    val seq: String,
    val status: String,
    val store_seq: String
)

data class GetStoreInfoImageStore(
    val image: List<GetStoreInfoImage>,
    val store: GetStoreInfoDetail
)

data class GetStoreInfoTime(
    val close: List<Any>,
    val `open`: List<Open>
)

data class GetStoreInfoImage(
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

data class GetStoreInfoDetail(
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
    val brand_seq: Any,
    val cate1: String,
    val cate1Name: GetStoreInfoCate1Name,
    val cate2: String,
    val cate3: String,
    val contents: String,
    val insertDate: String,
    val insertID: String,
    val kind: String,
    val likes: Boolean,
    val memo: Any,
    val modifyDate: String,
    val modifyID: String,
    val openDate: String,
    val seq: String,
    val status: String,
    val storeName: String
)

data class GetStoreInfoCate1Name(
    val majorCode: String,
    val majorName: String,
    val minorCode: String,
    val minorName: String
)

data class Open(
    val day: String,
    val day_name: String,
    val endMin: String,
    val endTime: String,
    val kind: String,
    val kind_name: String,
    val seq: String,
    val startMin: String,
    val startTime: String,
    val store_seq: String
)