package com.goodchoice.android.ohneulen.ui.search

import android.os.Parcelable
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.model.SearchStore
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import timber.log.Timber
import java.lang.Exception

class SearchViewModel(private val networkService: NetworkService) :
    ViewModel() {
    var searchEditText = ""
    var kakaoMapPoint = MutableLiveData<MapPoint>()
    var toastMessage = MutableLiveData<Boolean>(false)


    var mNetworkService = networkService
    var searchStoreList = MutableLiveData<List<SearchStore>>()
    val searchStoreAdapter = SearchStoreAdapter()

    val filterHashMap = HashMap<Int, String>()

    val tempCateOhneulenData = mutableListOf<OhneulenData>()

    //recyclerview state
    var recyclerViewState: Parcelable?=null

    //서버로 전송할 데이터
    var cate = mutableListOf<String>()
    var option = mutableListOf<String>()
    var openTime = mutableListOf<String>()
    var sort = mutableListOf<String>()
    var addrx = mutableListOf<Double>()
    val addry = mutableListOf<Double>()


    //카테고리
    var mainCategoryPosition = MutableLiveData<Int>(0)
    var subCategoryPosition = 0

//    val mainCategory = initData.mainCategory
//    val subCategoryList = initData.subCategory
//    var subCategory = MutableLiveData<List<OhneulenData>>(subCategoryList[0])

    //정렬
    var checkSortRating = false
    var checkSortRecent = false

    //옵션
//    var mainOptionKind = initData.mainOptionKind
//    var subOptionKind = initData.subOptionKind
//
//    //요일
//    var timeDay = initData.timeDay


    fun getStoreSearchList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkService.requestStoreSearchList(
                    addry,
                    addrx,
                    cate,
                    option,
                    openTime,
                    sort
                )
                searchStoreList.postValue(response.resultData)


            } catch (e: Throwable) {
                Timber.e(e.toString())
            }
        }
    }


    fun getSearchMapData() {
        CoroutineScope(Dispatchers.IO).launch {
            val y: Double
            val x: Double

            val addressResponse = networkService.requestKakaoAddress(address = searchEditText)
            if (addressResponse.documents.isEmpty()) {
                val keywordResponse =
                    networkService.requestKakaoKeyword(keyword = searchEditText)
                if (keywordResponse.documents.isNotEmpty()) {
                    y = keywordResponse.documents[0].y.toDouble()
                    x = keywordResponse.documents[0].x.toDouble()
//                    Timber.e("${y} , ${x}")
                } else {
                    toastMessage.postValue(true)
                    return@launch
                }
            } else {
                y = addressResponse.documents[0].y.toDouble()
                x = addressResponse.documents[0].x.toDouble()
            }
            kakaoMapPoint.postValue(MapPoint.mapPointWithGeoCoord(y, x))
//            kakaoMapPoint = MutableLiveData<MapPoint>()
        }
    }


    fun currentLocationData(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            kakaoMapPoint.postValue(MapPoint.mapPointWithGeoCoord(latitude, longitude))
//            kakaoMapPoint = MutableLiveData<MapPoint>()
        }
    }

    fun filterSubmit() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkService.requestStoreSearchList(
                    addry,
                    addrx,
                    cate,
                    option,
                    openTime,
                    sort
                )
                searchStoreList.postValue(response.resultData)

            } catch (e: Exception) {
                Timber.e(e.toString())
            }
//            cate.clear()
//            option.clear()
//            openTime.clear()
//            sort.clear()
            mainCategoryPosition.postValue(0)
        }
    }


    val loading = MutableLiveData<Boolean>()

}