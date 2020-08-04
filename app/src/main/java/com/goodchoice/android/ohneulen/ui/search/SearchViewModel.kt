package com.goodchoice.android.ohneulen.ui.search

import android.view.View
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.util.constant.ConstList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint

class SearchViewModel(private val networkService: NetworkService, initData: InitData) :
    ViewModel() {

    var searchEditText = ""
    var kakaoMapPoint = MutableLiveData<MapPoint>()
    var toastMessage = MutableLiveData<Boolean>(false)

    //    var searchStoreList: LiveData<MutableList<Store>> = liveData(Dispatchers.IO) {
//        loading.postValue(true)
//        emit(getStore())
//    }
    var searchStoreList = MutableLiveData<List<Store>>()
    val searchStoreAdapter = SearchStoreAdapter()

    val filterHashMap=HashMap<Int,String>()



    var mainCategoryPosition = MutableLiveData<Int>(0)
    var subCategoryPosition = 0
//    val categoryList = MutableLiveData(initData.mainCategoryList)



    val mainCategory = initData.mainCategoryList
    val subCategoryList = initData.subCategoryList
    var subCategory=MutableLiveData<List<Category>>(subCategoryList[0])



    fun getStoreList() {
        viewModelScope.launch(Dispatchers.IO) {
            val storeListResponse = networkService.requestGetStoreList()
            if (storeListResponse.resultCode == "000") {
                searchStoreList.postValue(storeListResponse.resultData)
            }
        }
    }


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