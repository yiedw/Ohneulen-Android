package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
data class StoreMenu(
    val seq:String,
    val store_seq:String,
    val title:String,
    val price:String,
    val contents:String,
    val status:String,
    val photoURL:String
)

//fun getStoreMenu():MutableList<StoreMenu>{
//    val sampleStoreMenuList= mutableListOf<StoreMenu>()
//    for(i in 0..10){
//        sampleStoreMenuList.add(
//            StoreMenu("i","shop$i", "아메리카노$i","3000원","어쩌구저쩌구","판매중")
//        )
//    }
//    return sampleStoreMenuList
//}