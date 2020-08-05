package com.goodchoice.android.ohneulen.data.model

data class FAQ(
    val seq:String,
    val classification:String,
    val title:String,
    val content:String,
    var check:Boolean=false
)

fun getFAQ(): List<FAQ> {
    val FAQlist= mutableListOf<FAQ>()
    for(i in 0..6){
        FAQlist.add(FAQ("$i","[분류]","자주찾는질문"+i,"내용"+i))
    }
    return FAQlist
}