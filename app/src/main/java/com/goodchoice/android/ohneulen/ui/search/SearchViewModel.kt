package com.goodchoice.android.ohneulen.ui.search

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.model.getPartner
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import timber.log.Timber

class SearchViewModel(private val networkService: NetworkService) : ViewModel() {
    companion object {
        var subCategory = MutableLiveData<MutableList<String>>()
        var mainCategory: List<String> = LoginViewModel.mainCategory
        var subCategoryList = LoginViewModel.subCategory
    }

    var searchEditText = ""
    var kakaoMapPoint = MutableLiveData<MapPoint>()

    var toastMessage=MutableLiveData<Boolean>(false)

    val partnerList: LiveData<MutableList<Partner>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getPartner())
    }

    fun getData() {
        mainCategory = LoginViewModel.mainCategory
        subCategoryList = LoginViewModel.subCategory
        subCategory.value = subCategoryList[0]
    }

    fun searchMapData() {

        viewModelScope.launch(Dispatchers.IO) {
            val y: Double
            val x: Double
            val addressResponse = networkService.requestKakaoAddress(address = searchEditText)
            if (addressResponse.documents.isEmpty()) {
                val keywordResponse =
                    networkService.requestKakaoKeyword(keyword = searchEditText)
                if (keywordResponse.documents.isNotEmpty()) {
                    y = keywordResponse.documents[0].y.toDouble()
                    x = keywordResponse.documents[0].x.toDouble()
                } else {
                    toastMessage.postValue(true)
                    return@launch
                }
            } else {
                y = addressResponse.documents[0].y.toDouble()
                x = addressResponse.documents[0].x.toDouble()
            }

            kakaoMapPoint.postValue(MapPoint.mapPointWithGeoCoord(y, x))
        }
    }


    val loading = MutableLiveData<Boolean>()

}