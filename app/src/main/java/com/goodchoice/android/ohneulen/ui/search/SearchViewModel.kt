package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.model.Store
import com.goodchoice.android.ohneulen.model.getStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import timber.log.Timber

class SearchViewModel(private val networkService: NetworkService) : ViewModel() {

    var searchEditText = ""
    var kakaoMapPoint = MutableLiveData<MapPoint>()
    var toastMessage = MutableLiveData<Boolean>(false)

    val storeList: LiveData<MutableList<Store>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getStore())
    }

    val searchAdapter = SearchPartnerAdapter()

    fun searchMapData() {
        GlobalScope.launch(Dispatchers.IO) {
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
            return@launch
        }
    }


    val loading = MutableLiveData<Boolean>()

}