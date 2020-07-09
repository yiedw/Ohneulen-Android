package com.goodchoice.android.ohneulen.data.repository

import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.data.model.MainCategory
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class InitData(private val networkService: NetworkService) {

    private val memId = "aaa@aa.com"
    private val memPw = "qwer1234"
    var categoryList = mutableListOf<MainCategory>()

    init {
        tempLogin()
    }

    private fun tempLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()
            )
            getCategory()
        }
    }

    //카테고리
    private fun getCategory() {
        categoryList = mutableListOf<MainCategory>()
        CoroutineScope(Dispatchers.IO).launch {
            val mainCategoryResponse = networkService.requestCategory("category".toRequestBody())

            for (i in mainCategoryResponse.resultData.indices) {
                val subCategoryList = mutableListOf<Category>()
                CoroutineScope(Dispatchers.IO).async {
                    val subCategoryResponse = networkService.requestCategory(
                        mainCategoryResponse.resultData[i].minorCode.toRequestBody()
                    )
                    for (j in subCategoryResponse.resultData.indices) {
                        val subCategoryMajorCode = subCategoryResponse.resultData[j].majorCode
                        val subCategoryMinorCode = subCategoryResponse.resultData[j].minorCode
                        val subCategoryMinorName = subCategoryResponse.resultData[j].minorName
                        subCategoryList.add(
                            Category(
                                subCategoryMajorCode,
                                subCategoryMinorCode,
                                subCategoryMinorName,
                                false
                            )
                        )
                    }
                }.await()
                val mainMajorCode = mainCategoryResponse.resultData[i].majorCode
                val mainMinorCode = mainCategoryResponse.resultData[i].minorCode
                val mainMinorName = mainCategoryResponse.resultData[i].minorName
                categoryList.add(
                    MainCategory(mainMajorCode, mainMinorCode, mainMinorName, subCategoryList)
                )
            }
            Timber.e(categoryList.toString())
        }
    }
}