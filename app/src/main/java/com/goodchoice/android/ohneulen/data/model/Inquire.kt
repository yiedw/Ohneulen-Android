package com.goodchoice.android.ohneulen.data.model

data class Inquire(
    val seq: String,
    val title: String,
    val content: String,
    val nickName: String,
    val status: String,
    val date: String,
    val answer: String
)

fun getInquire(): List<Inquire> {
    val inquireList = mutableListOf<Inquire>()
    for (i in 0..10) {
        inquireList.add(
            Inquire(
                "$i",
                "문의제목제목" + i,
                "내용은?",
                "닉네임",
                "답변완료" + i,
                "2020.02.31",
                "답변" + i
            )
        )
    }
    return inquireList
}