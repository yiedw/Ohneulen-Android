package com.goodchoice.android.ohneulen.model

data class PartnerMenu(
    val name: String,
    val price: String,
    val origin: String,
    val image: String
)

fun getPartnerMenu():MutableList<PartnerMenu>{
    val samplePartnerMenuList= mutableListOf<PartnerMenu>()
    for(i in 0..10){
        samplePartnerMenuList.add(
            PartnerMenu("아메리카노"+i,"3000원","에티오피아","image")
        )
    }

    return samplePartnerMenuList
}