package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.model.getPartner
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers

class SearchViewModel(private val networkService: NetworkService) : ViewModel() {
    companion object {
        var subCategory = MutableLiveData<MutableList<String>>()
        var mainCategory: List<String> = LoginViewModel.mainCategory
        var subCategoryList = LoginViewModel.subCategory
    }

    val partnerList: LiveData<MutableList<Partner>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getPartner())
    }

    fun getData() {
        mainCategory = LoginViewModel.mainCategory
        subCategoryList = LoginViewModel.subCategory
        subCategory.value= subCategoryList[0]
    }


    val loading = MutableLiveData<Boolean>()

}