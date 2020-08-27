package com.goodchoice.android.ohneulen.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.adapter.FAQAdapter
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
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
    var mypageReviewAdapter = ReviewAdapter(false)
    var mypageReviewList: LiveData<List<Review>> = liveData(Dispatchers.IO) {
        emit(getReview())
    }

    //문의
    var mypageInquireAdapter = InquireAdapter()
    var mypageInquireList: LiveData<List<Inquire>> = liveData(Dispatchers.IO) {
        emit(getInquireList())
    }

    //    var mypageInquireList: LiveData<List<Inquire>> = liveData(Dispatchers.IO) {
//        emit(getInquire())
//    }
    private suspend fun getInquireList(): List<Inquire> {
        var tempInquireList = listOf<Inquire>()
        try {
            val response = networkService.requestGetInquire()
            if (response.resultCode == ConstList.SUCCESS) {
                tempInquireList = response.resultData
            } else if (response.resultCode == ConstList.REQUIRE_LOGIN) {
                LoginViewModel.isLogin.postValue(false)
            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
        return tempInquireList
    }


    //자주 찾는 질문
//    var mypageFAQAdapter= FAQAdapter()
    var mypageFAQList = MutableLiveData(getFAQ())

}