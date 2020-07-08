package com.goodchoice.android.ohneulen.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.adapter.ReviewAdapter
import com.goodchoice.android.ohneulen.data.model.Inquire
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.data.model.getInquire
import com.goodchoice.android.ohneulen.data.model.getReview
import kotlinx.coroutines.Dispatchers

class MyPageViewModel : ViewModel() {

    val goodAdapter = MyPageGoodAdapter()

    var mypageReviewAdapter = ReviewAdapter()
    var mypageReviewList: LiveData<List<Review>> = liveData(Dispatchers.IO) {
        emit(getReview())
    }

    var mypageInquireAdapter = InquireAdapter()
    var mypageInquireList: LiveData<List<Inquire>> = liveData(Dispatchers.IO) {
        emit(getInquire())
    }
}