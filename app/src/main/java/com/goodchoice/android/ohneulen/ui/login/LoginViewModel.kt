package com.goodchoice.android.ohneulen.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBarFragment
import com.goodchoice.android.ohneulen.ui.mypage.MyPageFragment
import com.goodchoice.android.ohneulen.util.Event
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

class LoginViewModel(private val networkService: NetworkService,application: Application) :
    AndroidViewModel(application){

    var emailClick=true

    var isLogin = MutableLiveData(false)

    var memEmail = "aaa@aa.com"
    var memPw = "qwer1234"

    val loginErrorToast=MutableLiveData<Event<Boolean>>()


    fun login(check:Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val loginResponse = networkService.requestLogin(
                memEmail.toRequestBody(), memPw.toRequestBody()

            )
            if (loginResponse.resultCode == "000" || loginResponse.resultCode == "021") {
                isLogin.postValue(true)
                replaceMainFragment(MyPageFragment.newInstance())
                replaceAppbarFragment(MyPageAppBarFragment.newInstance())
                if(check){
                    //토큰 저장
                }
            }
            //로그인 실패
            else{
                loginErrorToast.postValue(Event(true))
            }
        }
    }

    fun loginTest() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = networkService.requestLoginTest()
            if (response.resultCode == "100") {
                isLogin.postValue(true)
            } else {
                isLogin.postValue(false)
            }
        }
    }

    fun logoutTest() {
        CoroutineScope(Dispatchers.IO).launch {
            networkService.requestLogoutTest()
            isLogin.postValue(false)
        }
    }

}

