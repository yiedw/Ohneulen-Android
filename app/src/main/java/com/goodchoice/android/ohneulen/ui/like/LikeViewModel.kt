package com.goodchoice.android.ohneulen.ui.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goodchoice.android.ohneulen.data.model.LikeStore
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.search.SearchStoreAdapter
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LikeViewModel(private val networkService: NetworkService) : ViewModel() {


    var likeStoreAdapter = LikeStoreAdapter()
    var mNetworkService = networkService
    var likeStoreList = MutableLiveData<List<LikeStore>>()
    var loginCheck = MutableLiveData<Boolean>(true)
    fun getStoreLikeList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkService.requestGetMemberLike()
                if (response.resultCode == ConstList.SUCCESS) {
                    if (likeStoreList != response.resultData) {
                        likeStoreList.postValue(response.resultData)
                    }
                } else if (response.resultCode == ConstList.REQUIRE_LOGIN) {
                    loginCheck.postValue(false)
                }
            } catch (e: Exception) {
                Timber.e(e.toString())
            }
        }
    }
}