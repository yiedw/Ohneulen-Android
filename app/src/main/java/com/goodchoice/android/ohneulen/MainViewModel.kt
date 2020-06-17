package com.goodchoice.android.ohneulen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    //서치 결과 저장
    private var liveSearchResult: MutableLiveData<String> = MutableLiveData()
    fun getSearchResult():MutableLiveData<String>{
        return liveSearchResult
    }
    fun setSearchResult(value:String){
        liveSearchResult.value=value
    }
}
