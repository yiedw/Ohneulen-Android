package com.goodchoice.android.ohneulen.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.ui.adapter.FAQAdapter
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import kotlinx.coroutines.Dispatchers

class MyPageViewModel : ViewModel() {

//    val goodAdapter = MyPageGoodAdapter()


    //후기
    var mypageReviewAdapter = ReviewAdapter(false)
    var mypageReviewList: LiveData<List<Review>> = liveData(Dispatchers.IO) {
        emit(getReview())
    }

    //문의
    var mypageInquireAdapter = InquireAdapter()
    var mypageInquireList=MutableLiveData<List<Inquire>>()
//    var mypageInquireList: LiveData<List<Inquire>> = liveData(Dispatchers.IO) {
//        emit(getInquire())
//    }

    //자주 찾는 질문
//    var mypageFAQAdapter= FAQAdapter()
    var mypageFAQList = MutableLiveData(getFAQ())

}