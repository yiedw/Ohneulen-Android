package com.goodchoice.android.ohneulen.ui.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.data.model.getPhoto
import com.goodchoice.android.ohneulen.data.model.getStoreMenu
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


}