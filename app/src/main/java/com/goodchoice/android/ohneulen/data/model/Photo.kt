package com.goodchoice.android.ohneulen.data.model

data class Photo(
    val kind: String,
    val mirror_seq: String,
    val photoURL: String
)

fun getPhoto(): MutableList<Photo> {
    val samplePhotoList = mutableListOf<Photo>()
    for (i in 0..10) {
        samplePhotoList.add(
            Photo(
                "메뉴",
                i.toString(),
                "https://www.rfa.org/korean/weekly_program/ac74ac15d558ac8c-c0bdc2dcb2e4/healthylife-07232018093955.html/py_cold_noodle_b.jpg"
            )
        )
    }
    return samplePhotoList

}