package com.goodchoice.android.ohneulen.ui.login

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.KoinComponent
import timber.log.Timber

class LoginViewModel(private val networkService: NetworkService) : ViewModel(), KoinComponent {

    //temp
//    companion object {
//    }

    var memId = "aaa@aa.com"
    var memPw = "qwer1234"
    val mainCategory = MutableLiveData<MutableList<String>>()
    val subCategory = MutableLiveData<MutableList<MutableList<String>>>()
    fun a() {
        viewModelScope.launch(Dispatchers.IO) {
            val loginResponse = networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()

            )
            Timber.e(loginResponse.toString())
            Timber.e(App.cookie.toString())
        }
    }

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = networkService.requestCategory("category".toRequestBody())
            val mainCategoryList= mutableListOf<String>()
            val subCategoryList= mutableListOf<MutableList<String>>()
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
            mainCategory.postValue(mainCategoryList)
            subCategory.postValue(subCategoryList)
            Timber.e(mainCategory.value.toString())
            Timber.e(subCategory.value.toString())

        }
    }


    fun logoutTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = networkService.logoutTest()
            Timber.e(response.toString())
            Timber.e(App.cookie.toString())

        }
    }
}

