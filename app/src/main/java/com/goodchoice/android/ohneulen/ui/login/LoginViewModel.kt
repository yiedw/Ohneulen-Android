package com.goodchoice.android.ohneulen.ui.login

import android.widget.Button
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

    var isLogin = MutableLiveData(false)

    var memId = "aaa@aa.com"
    var memPw = "qwer1234"

    //    private val mainCategory = MutableLiveData<MutableList<String>>()
//    private val subCategory = MutableLiveData<MutableList<MutableList<String>>>()
    fun a() {
        viewModelScope.launch(Dispatchers.IO) {
            val loginResponse = networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()

            )
            if (loginResponse.resultCode == "000" || loginResponse.resultCode=="021") {
                Timber.e("로그인 성공")
                isLogin.postValue(true)
            }
            Timber.e(loginResponse.toString())
        }
    }

    fun loginTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = networkService.requestLoginTest()
            Timber.e(response.toString())
            if (response.resultCode == "100") {
                isLogin.postValue(true)
                Timber.e("로그인 되있음")
            } else {
                isLogin.postValue(false)
                Timber.e("로그인 안되있음")
            }
        }
    }

//    fun test() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = networkService.requestCategory("category".toRequestBody())
//            val mainCategoryList= mutableListOf<String>()
//            val subCategoryList= mutableListOf<MutableList<String>>()
//            for (i in response.resultData.indices) {
//                val subList = mutableListOf<String>()
//                viewModelScope.async(Dispatchers.IO) {
//                    val subResponse =
//                        networkService.requestCategory(response.resultData[i].minorCode.toRequestBody())
//                    for (j in subResponse.resultData.indices) {
//                        subList.add(subResponse.resultData[j].minorName)
//                    }
//                }.await()
//                mainCategoryList.add(response.resultData[i].minorName)
//                subCategoryList.add(subList)
//            }
//            mainCategory.postValue(mainCategoryList)
//            subCategory.postValue(subCategoryList)
//            Timber.e(mainCategory.value.toString())
//            Timber.e(subCategory.value.toString())
//
//        }
//    }


    fun logoutTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = networkService.requestLogoutTest()
            isLogin.postValue(false)
            Timber.e(isLogin.value.toString())
            Timber.e(response.toString())
        }
    }
}

