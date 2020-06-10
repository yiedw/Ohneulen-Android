package com.goodchoice.android.ohneulen.ui.login

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.model.LoginData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.KoinComponent
import timber.log.Timber

class LoginViewModel(val networkService: NetworkService) : ViewModel(), KoinComponent {

    var memId = ""
    var memPw = ""
    fun a() {
        viewModelScope.launch(Dispatchers.IO) {
            val body: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("memId", memId)
                .addFormDataPart("memPw", memPw)
                .build()
            val loginResponse = networkService.ohneulenLogin(
                body
            )
            Timber.e(loginResponse.toString())
        }
    }
}

