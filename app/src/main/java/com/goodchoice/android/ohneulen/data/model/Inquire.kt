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
                "본인 맞고 폰이 두 개라서 전화번호 바꾸려고 하는데 안 된다고 하네요 자꾸 확인 좀 해 주삼",
                "닉네임",
                "답변완료" + i,
                "2020.02.31",
                "오늘의 답변 \n \n새우깡 고객님! \n보내 주신 문의 확인 후 전화번호 변경 처리해 드렸습니다. 감사합니다."
            )
        )
    }
    return inquireList
}