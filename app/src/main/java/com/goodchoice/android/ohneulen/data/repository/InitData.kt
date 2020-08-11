package com.goodchoice.android.ohneulen.data.repository

import androidx.lifecycle.MutableLiveData
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.getOhneulenData
import com.goodchoice.android.ohneulen.util.getOhneulenSubData
import kotlinx.coroutines.*
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class InitData(private val networkService: NetworkService) {

    private val memId = "aaa@aa.com"
    private val memPw = "qwer1234"

    //카테고리
    var mainCategory = mutableListOf<OhneulenData>()
    var subCategory = mutableListOf<List<OhneulenData>>()

    //옵션
    var mainOptionKind = mutableListOf<OhneulenData>()
    var subOptionKind = mutableListOf<List<OhneulenData>>()

    //요일
    var timeDay = mutableListOf<OhneulenData>()


    //옵션

    init {
        tempLogin()

        //옵션 가져오기
        getOptionKind()
        //카테고리 가져오기
        getCategory()
        //요일 가져오기
        CoroutineScope(Dispatchers.IO).launch {
            timeDay = getOhneulenData(networkService, ConstList.TIME_DAY)
        }

    }

    private fun tempLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            networkService.requestLogin(
                memId.toRequestBody(), memPw.toRequestBody()
            )
            LoginViewModel.isLogin.postValue(true)
        }
    }


    private fun getCategory() {
        CoroutineScope(Dispatchers.IO).launch {
            mainCategory = getOhneulenData(networkService, ConstList.CATEGORY)
            for (i in mainCategory.indices) {
                val tempList = getOhneulenData(networkService, mainCategory[i].minorCode)
                subCategory.add(tempList)
            }
            subCategory = getOhneulenSubData(networkService, mainCategory)
            Timber.e(subCategory.toString())
        }
    }

    private fun getOptionKind() {
        CoroutineScope(Dispatchers.IO).launch {
            mainOptionKind = getOhneulenData(networkService, ConstList.OPTION_KIND)
            for (i in mainOptionKind.indices) {
                val tempList = getOhneulenData(networkService, mainOptionKind[i].minorCode)
                subOptionKind.add(tempList)
            }
            subOptionKind = getOhneulenSubData(networkService, mainOptionKind)
//            Timber.e(subOptionKind.toString())
        }
    }

}