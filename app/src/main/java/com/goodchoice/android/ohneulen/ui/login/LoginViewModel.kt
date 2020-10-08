package com.goodchoice.android.ohneulen.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.util.Event
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(private val networkService: NetworkService, application: Application) :
    AndroidViewModel(application) {

    companion object {
        var isLogin = MutableLiveData(false)
    }

    var emailClick = true
    var memberEmail = "이메일"

    val loginErrorToast = MutableLiveData<Event<Boolean>>()


    fun login(memEmail: String, memPw: String, check: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginResponse = networkService.requestLogin(
                    memEmail.toRequestBody(), memPw.toRequestBody()
                )
                if (loginResponse.resultCode == "000" || loginResponse.resultCode == "021") {
                    isLogin.postValue(true)
                    memberEmail = memEmail
//                    replaceMainFragment(MyPage.newInstance())
//                    replaceAppbarFragment(MyPageAppBar.newInstance())
                    if (check) {
                        //토큰 저장
                    }
                }
                //로그인 실패
                else {
                    loginErrorToast.postValue(Event(true))
                }

            } catch (e: Throwable) {
                loginErrorToast.postValue(Event(true))

            }
        }
    }


    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            networkService.requestLogoutTest()
            isLogin.postValue(false)
        }
    }

}

