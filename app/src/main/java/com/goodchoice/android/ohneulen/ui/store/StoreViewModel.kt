package com.goodchoice.android.ohneulen.ui.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent

class StoreViewModel(networkService: NetworkService) : ViewModel() {
    val storeMenuDetailViewAdapter = StoreMenuDetailAdapter()
    val storeMenuList: LiveData<MutableList<StoreMenu>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getStoreMenu())
    }
    val loading = MutableLiveData<Boolean>()

    var storeMenuPhotoList: LiveData<MutableList<Photo>> = liveData(Dispatchers.IO) {
        emit(getPhoto())
    }
    val storeInfo= getStore()

    //menuDetail 클릭했을때 클릭한 곳으로 이동
    var index:Int=0

    //이미지 삽입
    val image1= R.drawable.food_sample1
    val image2= R.drawable.food_sample2
    val image3= R.drawable.food_sample3
    val image4= R.drawable.food_sample4


}