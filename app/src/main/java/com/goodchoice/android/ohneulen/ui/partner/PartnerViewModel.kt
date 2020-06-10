package com.goodchoice.android.ohneulen.ui.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.model.PartnerMenu
import com.goodchoice.android.ohneulen.model.getPartnerMenu
import kotlinx.coroutines.Dispatchers

class PartnerViewModel :ViewModel(){
    val partnerMenuList: LiveData<MutableList<PartnerMenu>> = liveData(Dispatchers.IO){
        loading.postValue(true)
        emit(getPartnerMenu())
    }
    val loading=MutableLiveData<Boolean>()

}