package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.model.getPartner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class SearchViewModel(val networkService: NetworkService) :ViewModel(){

    val partnerList:LiveData<MutableList<Partner>> = liveData (Dispatchers.IO){
        loading.postValue(true)
        emit(getPartner())
    }

    fun getMainCategory(){
        viewModelScope.launch (Dispatchers.IO){
            val response=networkService.mainCategory("category".toRequestBody())
            Timber.e(response.toString())
        }
    }


    val loading= MutableLiveData<Boolean>()

}