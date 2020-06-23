package com.goodchoice.android.ohneulen.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapView
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class MainViewModel(private val networkService: NetworkService) : ViewModel() {

    //categoryData
    var mainCategory = listOf<String>()
    var subCategory = listOf<MutableList<String>>()
    var subCategoryDetail = MutableLiveData<MutableList<String>>()

    var memId = "aaa@aa.com"
    var memPw = "qwer1234"

    fun a() {
        viewModelScope.launch(Dispatchers.IO) {
            val loginResponse = networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()
            )
            test()
            Timber.e(loginResponse.toString())
            Timber.e(App.cookie.toString())
        }
    }

    private fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = networkService.requestCategory("category".toRequestBody())
            val mainCategoryList = mutableListOf<String>()
            val subCategoryList = mutableListOf<MutableList<String>>()
            for (i in response.resultData.indices) {
                val subList = mutableListOf<String>()
                viewModelScope.async(Dispatchers.IO) {
                    val subResponse =
                        networkService.requestCategory(response.resultData[i].minorCode.toRequestBody())
                    for (j in subResponse.resultData.indices) {
                        subList.add(subResponse.resultData[j].minorName)
                    }
                }.await()
                mainCategoryList.add(response.resultData[i].minorName)
                subCategoryList.add(subList)
            }
            mainCategory = mainCategoryList
            subCategory = subCategoryList
            subCategoryDetail.postValue(subCategory[0])
            Timber.e(mainCategory.toString())
            Timber.e(subCategory.toString())

        }

    }

    // home -> search 로 data 이동
    var searchEditText = ""

    //filter -> search 로 data 이동
    var filterDataList = mutableListOf<String>()




}
