package com.goodchoice.android.ohneulen.ui.store

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class StoreViewModel(private val networkService: NetworkService) : ViewModel() {
    val storeMenuList: LiveData<MutableList<StoreMenu>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getStoreMenu())
    }

    var storeDetail=MutableLiveData<StoreDetail>()
    //storeDetail 가져오기
    fun getStoreDetail(storeSeq:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val storeDetailResponse=networkService.requestGetStoreInfo(
                storeSeq.toRequestBody()
            )
            if(storeDetailResponse.resultCode=="000"){
                storeDetail.postValue(storeDetailResponse.resultData)
            }

        }
    }

    //menuDetail 클릭했을때 클릭한 곳으로 이동
    var menuIndex = 0

    val loading = MutableLiveData<Boolean>()


    var storeImageList: LiveData<MutableList<Photo>> = liveData(Dispatchers.IO) {
        emit(getPhoto())
    }


    var storeImageDetailList: LiveData<MutableList<Photo>> = liveData(Dispatchers.IO) {
        emit(getPhotoDetail())
    }
    var storeImageDetailIndex = 0


//    var storeReviewList: LiveData<List<Review>> = liveData(Dispatchers.IO) {
//        emit(getReview())
//    }
//    var storeReviewList=MutableLiveData<List<Review>>()
//    private fun getStoreReviewList(){
//
//    }

    var storeReviewAdapter = ReviewAdapter()



}