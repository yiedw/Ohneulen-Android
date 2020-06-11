package com.goodchoice.android.ohneulen.ui.login

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.KoinComponent
import timber.log.Timber

class LoginViewModel(private val networkService: NetworkService) : ViewModel(), KoinComponent {

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

    fun test(){
        viewModelScope.launch (Dispatchers.IO){
            val response=networkService.mainCategory("category".toRequestBody())
            Timber.e(response.toString())
        }
    }

    fun logoutTest(){
        viewModelScope.launch (Dispatchers.IO){
            val response=networkService.logoutTest()
            Timber.e(response.toString())
            Timber.e(App.cookie.toString())

        }
    }
}

