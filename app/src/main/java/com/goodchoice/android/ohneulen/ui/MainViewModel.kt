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
    var currentLocationSearch=false
    //검색인지 하단 지도탭인지클릭인지
    var searchCheck=false

}
