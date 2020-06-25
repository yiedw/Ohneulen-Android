package com.goodchoice.android.ohneulen.data.model

data class StoreMenu(
    val seq:String,
    val shopSeq:String,
    val title:String,
    val price:String,
    val status:String
)

fun getStoreMenu():MutableList<StoreMenu>{
    val sampleStoreMenuList= mutableListOf<StoreMenu>()
    for(i in 0..10){
        sampleStoreMenuList.add(
            StoreMenu("i","shop$i","아메리카노","3000원","판매중")
        )
    }
    return sampleStoreMenuList
}