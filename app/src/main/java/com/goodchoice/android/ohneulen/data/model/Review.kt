package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

@Keep
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
    for (i in 0..10) {
        reviewList.add(
            Review(
                i.toString()+"내용",
                listOf(),
                "2020-09-16",
                "1",
                "2020-09-17",
                "5",
                "4",
                "3",
                "2",
                "1",
                "3",
                "3",
                "3",
                "4",
                "5"
            )
        )
    }
    return reviewList
}