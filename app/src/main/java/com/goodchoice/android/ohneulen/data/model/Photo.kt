package com.goodchoice.android.ohneulen.data.model

data class Photo(
    val seq: String,
    val kind: String,
    val mirror_seq: String,
    val photoURL: String
)

fun getPhoto(): MutableList<Photo> {
    val samplePhotoList = mutableListOf<Photo>()
    for (i in 0..10) {
        samplePhotoList.add(
            Photo(
                "$i",
                "메뉴",
                i.toString(),
                "data:image/jpeg;base64,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"
            )
        )
    }
    return samplePhotoList
}

fun getPhotoDetail(): MutableList<Photo> {
    val samplePhotoList = mutableListOf<Photo>()
    for (i in 0..10) {
        samplePhotoList.add(
            Photo(
                "$i",
                "메뉴",
                i.toString(),
                "https://th4.tmon.kr/thumbs/image/d26/07c/a58/c4aa0a377_700x700_95_FIT.jpg"
            )
        )
    }
    return samplePhotoList
}

fun getProfileImage(): Photo {
    return Photo(
        "a",
        "회원",
        "0",
        "https://lh3.googleusercontent.com/proxy/wLWqgBMhdBh1RPjRT3iQk63S7flh6PAHsmhpQwbHLZ3d-rpR1aRube24VdobOlsqwgM4JZ6YBAkv-CuAKw21NZiG5OlqO3vPkmO80X3H8wJhsA"
    )
}
