package com.goodchoice.android.ohneulen.data.model

data class Category (
    val majorCode: String,
    val minorCode: String,
    val minorName: String,
    val subCategoryList: MutableList<OhneulenData> = mutableListOf()
)

