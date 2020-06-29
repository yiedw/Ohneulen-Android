package com.goodchoice.android.ohneulen.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class MainViewModel() : ViewModel() {

    // home -> search 로 data 이동
    var searchEditText = ""

    //search -> store 넘어갈때 맵뷰 잠시 없애기
    var searchMapView=MutableLiveData(true)


}
