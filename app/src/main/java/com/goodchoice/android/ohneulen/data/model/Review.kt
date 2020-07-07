package com.goodchoice.android.ohneulen.data.model

data class Review (
    val storeSeq:String,
    val memberSeq:String,
    val point:String,
    val content:String,
    val date:String
)

fun getReview():List<Review>{
    val reviewList= mutableListOf<Review>()
    for(i in 0..10){
        reviewList.add(Review("a","b","a","내용", "2020.05.18"))
    }
    return reviewList
}