package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep

//data class MainCategory(
//    val majorCode: String,
//    val minorCode: String,
//    val minorName: String,
//    val subCategoryList: MutableList<Category> = mutableListOf()
//)
@Keep
data class OhneulenData(
    val majorCode: String,
    val minorCode: String,
    val minorName: String,
    var check:Boolean
)

