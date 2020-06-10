package com.goodchoice.android.ohneulen.model

data class Partner(
    val code: String,
    val name: String,
    val image: String,
    val rating: String,
    val numberRatings: String,
    val businessTime: String,
    val breakTime: String,
    val new: Boolean
)

fun getPartner(): MutableList<Partner> {
    val samplePartnerList = mutableListOf<Partner>()
    for (i in 0..10) {
        samplePartnerList.add(
            Partner(
                i.toString(), "샘플$i",
                "image", "4.5", "101", "영업시간", "브레이크타임", true
            )
        )
    }
    return samplePartnerList
}
