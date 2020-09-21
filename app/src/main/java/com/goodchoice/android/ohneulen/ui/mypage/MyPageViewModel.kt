package com.goodchoice.android.ohneulen.ui.mypage

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MyPageViewModel(private val networkService: NetworkService) : ViewModel() {

//    val goodAdapter = MyPageGoodAdapter()


    //후기
    var mypageReviewList: LiveData<List<Review>> = liveData(Dispatchers.IO) {
        emit(getReview())
    }

    //문의
    var mypageInquireAdapter = InquireAdapter()
    var mypageInquireList = MutableLiveData<List<Inquire>>()
    var mypageInquireCode = MutableLiveData<String>()

    fun getInquireList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //001 -> 1:1문의
                val response = networkService.requestGetBoard(ConstList.MYPAGE_INQUIRE)
                if (response.resultCode == ConstList.SUCCESS) {
                    if (mypageFAQList.value != response.resultData) {
                        mypageInquireList.postValue(response.resultData)
                    }
                } else if (response.resultCode == ConstList.REQUIRE_LOGIN) {
                    LoginViewModel.isLogin.postValue(false)
                }
            } catch (e: Exception) {
                Timber.e(e.toString())
            }
        }
    }

    fun setInquireList(gubun1: String, title: String, contents: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                //001 -> 1:1 문의
                val response = networkService.requestSetBoard(
                    ConstList.MYPAGE_INQUIRE,
                    gubun1,
                    title,
                    contents
                )
                mypageInquireCode.postValue(response.resultCode)
                if (response.resultCode == ConstList.SUCCESS) {
                    getInquireList()
                }
            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
    }


    //자주 찾는 질문
//    var mypageFAQAdapter= FAQAdapter()
    var mypageFAQList = MutableLiveData(getFAQ())

}