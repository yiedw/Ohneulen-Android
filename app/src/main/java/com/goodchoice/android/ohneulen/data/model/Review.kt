package com.goodchoice.android.ohneulen.data.model

data class Review(
    val content: String,
    val imgList: List<Image>,
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

fun getReview(): List<Review> {
    val reviewList = mutableListOf<Review>()
//    for (i in 0..10) {
//        reviewList.add(Review(i.toString(), "a", "b", "a", "내용", "2020.05.18"))
//    }
    return reviewList
}