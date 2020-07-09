package com.goodchoice.android.ohneulen.data.model

data class FAQ(
    val classification:String,
    val title:String,
    val content:String
)

fun getFAQ(): List<FAQ> {
    val FAQlist= mutableListOf<FAQ>()
    for(i in 0..6){
        FAQlist.add(FAQ("[분류]","자주찾는질문"+i,"내용"+i))
    }
    return FAQlist
}