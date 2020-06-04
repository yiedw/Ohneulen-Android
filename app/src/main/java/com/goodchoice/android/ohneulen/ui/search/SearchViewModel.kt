package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goodchoice.android.ohneulen.model.Restaurant
import com.goodchoice.android.ohneulen.model.getRestaurant
import kotlinx.coroutines.Dispatchers

class SearchViewModel :ViewModel(){

    val restaurantList:LiveData<MutableList<Restaurant>> = liveData (Dispatchers.IO){
        loading.postValue(true)
        emit(getRestaurant())
    }

    val loading= MutableLiveData<Boolean>()

}