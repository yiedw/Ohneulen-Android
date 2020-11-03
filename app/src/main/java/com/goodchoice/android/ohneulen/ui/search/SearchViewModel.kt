package com.goodchoice.android.ohneulen.ui.search

import android.os.Parcelable
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.model.SearchStore
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import timber.log.Timber
import java.lang.Exception

class SearchViewModel(private val networkService: NetworkService) :
    ViewModel() {
    var searchEditText = ""                                 //검색한 곳 저장
    var kakaoMapPoint = MutableLiveData<MapPoint>()                 //카카오맵 좌표
    var toastMessage = MutableLiveData<Boolean>(false)      //토스트 메시지

    //(store->search)경우 rv부분 새로고침
    var refreshCheck = MutableLiveData<Boolean>(false)


    var mNetworkService = networkService
    var searchStoreList = MutableLiveData<List<SearchStore>>()

    //서치페이지에 처음들어왔는지 여부
    //처음이면 강남역을 넣어줌
    var searchFirst = true

    val tempCateOhneulenData = mutableListOf<OhneulenData>()

    //recyclerview state
    var recyclerViewState: Parcelable? = null

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


    //정렬
    var checkSortRating = false
    var checkSortRecent = false

    //스토어 리스트를 가져오는 함수
    fun getSearchStoreList() {
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
                if (LoginViewModel.isLogin.value!!) {
                    val likesResponse = networkService.requestGetMemberLike()
                    for (i in response.resultData.indices) {
                        for (j in likesResponse.resultData) {
                            if (response.resultData[i].seq == j.seq) {
                                response.resultData[i].like = true
                                break
                            }
                        }
                    }
                    //데이터가 다를 경우에만 전달
//                    Timber.e(response.resultData.toString())
//                    Timber.e(searchStoreList.value.toString())

                    if (response.resultData != searchStoreList.value) {
                        searchStoreList.postValue(response.resultData)
                    }
                } else {
                    if (response.resultData != searchStoreList.value) {
                        searchStoreList.postValue(response.resultData)
                    }
                }
            } catch (e: Throwable) {
                Timber.e(e.toString())
            }
        }
    }

    //좌표를 바탕으로 카카오맵을 가져오는 함수
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

    //현재 위치를 기반으로 카카오 데이터를가져옴
    fun currentLocationData(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            kakaoMapPoint.postValue(MapPoint.mapPointWithGeoCoord(latitude, longitude))
//            kakaoMapPoint = MutableLiveData<MapPoint>()
        }
    }

    //로딩여부확인
    val loading = MutableLiveData<Boolean>()

}