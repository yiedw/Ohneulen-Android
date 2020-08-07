package com.goodchoice.android.ohneulen.data.repository

import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class InitData(private val networkService: NetworkService) {

    private val memId = "aaa@aa.com"
    private val memPw = "qwer1234"
    var mainCategoryList = mutableListOf<Category>()
    var subCategoryList = mutableListOf<List<Category>>()

    init {
        tempLogin()
        getCategory()
    }

    private fun tempLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()
            )
            LoginViewModel.isLogin.postValue(true)
        }
    }

    //카테고리
    private fun getCategory() {
        CoroutineScope(Dispatchers.IO).launch {
            val mainCategoryResponse =
                networkService.requestOhneulenData(ConstList.CATEGORY.toRequestBody())

            for (i in mainCategoryResponse.resultData.indices) {
                val tempSubCategoryList = mutableListOf<Category>()
                CoroutineScope(Dispatchers.IO).async {
                    val subCategoryResponse = networkService.requestOhneulenData(
                        mainCategoryResponse.resultData[i].minorCode.toRequestBody()
                    )
                    for (j in subCategoryResponse.resultData.indices) {
                        val subCategoryMajorCode = subCategoryResponse.resultData[j].majorCode
                        val subCategoryMinorCode = subCategoryResponse.resultData[j].minorCode
                        val subCategoryMinorName = subCategoryResponse.resultData[j].minorName
                        tempSubCategoryList.add(
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
                if (i == 0) {
                    mainCategoryList.add(
                        Category(mainMajorCode, mainMinorCode, mainMinorName, true)
                    )
                } else {
                    mainCategoryList.add(
                        Category(mainMajorCode, mainMinorCode, mainMinorName, false)
                    )
                }
                subCategoryList.add(tempSubCategoryList)
            }
            Timber.e(mainCategoryList.toString())
        }
    }

    private fun getOptions() {
        CoroutineScope(Dispatchers.IO).launch {
            val mainResponse =
                networkService.requestOhneulenData(ConstList.OPTION_KIND.toRequestBody())

            for (i in mainResponse.resultData.indices) {
                val tempSubCategoryList = mutableListOf<Category>()
                CoroutineScope(Dispatchers.IO).async {
                    val subResponse = networkService.requestOhneulenData(
                        mainResponse.resultData[i].minorCode.toRequestBody()
                    )
                    for (j in subResponse.resultData.indices) {
                        val subMajorCode = subResponse.resultData[j].majorCode
                        val subMinorCode = subResponse.resultData[j].minorCode
                        val subMinorName = subResponse.resultData[j].minorName
                        tempSubCategoryList.add(
                            Category(
                                subMajorCode,
                                subMinorCode,
                                subMinorName,
                                false
                            )
                        )
                    }
                }.await()
                val mainMajorCode = mainResponse.resultData[i].majorCode
                val mainMinorCode = mainResponse.resultData[i].minorCode
                val mainMinorName = mainResponse.resultData[i].minorName
                if (i == 0) {
                    mainCategoryList.add(
                        Category(mainMajorCode, mainMinorCode, mainMinorName, true)
                    )
                } else {
                    mainCategoryList.add(
                        Category(mainMajorCode, mainMinorCode, mainMinorName, false)
                    )
                }
                subCategoryList.add(tempSubCategoryList)
            }
            Timber.e(mainCategoryList.toString())
        }
    }
}