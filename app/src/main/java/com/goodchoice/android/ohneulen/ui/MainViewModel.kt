package com.goodchoice.android.ohneulen.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import net.daum.mf.map.api.MapView

class MainViewModel : ViewModel() {

    //categoryData
    var mainCategory = MutableLiveData<MutableList<String>>()
    var subCategory = MutableLiveData<MutableList<MutableList<String>>>()

    // home -> search 로 data 이동
    var searchEditText=""

    //filter -> search 로 data 이동
    var filterDataList= mutableListOf<String>()


}
