package com.goodchoice.android.ohneulen.ui.login

import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBarFragment
import com.goodchoice.android.ohneulen.ui.mypage.MyPageFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.coroutines.CoroutineScope
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
    fun login(check:Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val loginResponse = networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()

            )
            if (loginResponse.resultCode == "000" || loginResponse.resultCode == "021") {
                isLogin.postValue(true)
                replaceMainFragment(MyPageFragment.newInstance())
                replaceAppbarFragment(MyPageAppBarFragment.newInstance())
                if(check){
                    //토큰 저장
                }
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
        CoroutineScope(Dispatchers.IO).launch {
            networkService.requestLogoutTest()
            isLogin.postValue(false)
        }
    }
}

