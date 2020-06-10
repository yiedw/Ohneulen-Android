package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.model.getPartner
import kotlinx.coroutines.Dispatchers

class SearchViewModel :ViewModel(){

    val partnerList:LiveData<MutableList<Partner>> = liveData (Dispatchers.IO){
        loading.postValue(true)
        emit(getPartner())
    }

    val loading= MutableLiveData<Boolean>()

}