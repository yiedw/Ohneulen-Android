package com.goodchoice.android.ohneulen.ui.store

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(networkService: NetworkService) : ViewModel() {
    val storeMenuList: LiveData<MutableList<StoreMenu>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getStoreMenu())
    }
    val loading = MutableLiveData<Boolean>()

    var storeMenuPhotoList: LiveData<MutableList<Photo>> = liveData(Dispatchers.IO) {
        emit(getPhoto())
    }

    var storeReviewList:LiveData<List<Review>> = liveData(Dispatchers.IO){
        emit(getReview())
    }
    var storeReviewAdapter= ReviewAdapter()

    val storeInfo = getStore()

    //menuDetail 클릭했을때 클릭한 곳으로 이동
    var index: Int = 0

    //이미지 삽입
    val image1 = R.drawable.food_sample1
    val image2 = R.drawable.food_sample2
    val image3 = R.drawable.food_sample3
    val image4 = R.drawable.food_sample4





}