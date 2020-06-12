package com.goodchoice.android.ohneulen.ui.login

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.model.FoodFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.KoinComponent
import timber.log.Timber

class LoginViewModel(private val networkService: NetworkService) : ViewModel(), KoinComponent {

    //temp
    companion object {
        val mainCategory= mutableListOf<String>()
        val subCategory= mutableListOf<MutableList<String>>()
    }

    var memId = "aaa@aa.com"
    var memPw = "qwer1234"
    fun a() {
        viewModelScope.launch(Dispatchers.IO) {
            val loginResponse = networkService.ohneulenLogin(
                memId.toRequestBody(), memPw.toRequestBody()
            )
            Timber.e(loginResponse.toString())
            Timber.e(App.cookie.toString())
        }
    }

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = networkService.category("category".toRequestBody())
            for (i in response.resultData.indices) {
                val subList = mutableListOf<String>()
                viewModelScope.async(Dispatchers.IO) {
                    val subResponse =
                        networkService.category(response.resultData[i].minorCode.toRequestBody())
                    for (j in subResponse.resultData.indices) {
                        subList.add(subResponse.resultData[j].minorName)
                    }
                }.await()
                mainCategory.add(response.resultData[i].minorName)
                subCategory.add(subList)
            }
            Timber.e(mainCategory.toString())
            Timber.e(subCategory.toString())

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

