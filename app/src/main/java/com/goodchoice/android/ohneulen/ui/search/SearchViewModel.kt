package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.model.getStore
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.util.ConstList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint

class SearchViewModel(private val networkService: NetworkService, initData: InitData) :
    ViewModel() {

    var searchEditText = ""
    var kakaoMapPoint = MutableLiveData<MapPoint>()
    var toastMessage = MutableLiveData<Boolean>(false)

    val searchStoreList: LiveData<MutableList<Store>> = liveData(Dispatchers.IO) {
        loading.postValue(true)
        emit(getStore())
    }
    val searchStoreAdapter = SearchStoreAdapter()

    val mainCategoryAdapter = SearchFilterAdapter(ConstList.MAIN_CATEGORY)
    val subCategoryAdapter = SearchFilterAdapter(ConstList.SUB_CATEGORY)
    var mainCategoryPosition = MutableLiveData<Int>(0)
    var subCategoryPosition=MutableLiveData<Int>(0)
    val categoryList = initData.categoryList


    val mainCategory = mainCategoryInit()
    var subCategory = MutableLiveData(categoryList[0].subCategoryList)

    private fun mainCategoryInit(): MutableList<OhneulenData> {

        val temp = mutableListOf<OhneulenData>()
        for (i in categoryList.indices) {
            temp.add(
                OhneulenData(
                    categoryList[i].majorCode,
                    categoryList[i].minorCode,
                    categoryList[i].minorName
                )
            )
        }
        return temp
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