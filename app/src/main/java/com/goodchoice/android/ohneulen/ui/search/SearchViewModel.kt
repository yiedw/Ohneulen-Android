package com.goodchoice.android.ohneulen.ui.search

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.repository.InitData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class SearchViewModel(private val networkService: NetworkService, initData: InitData) :
    ViewModel() {

    var searchEditText = ""
    var kakaoMapPoint = MutableLiveData<MapPoint>()
    var toastMessage = MutableLiveData<Boolean>(false)


    var searchStoreList = MutableLiveData<List<Store>>()
    val searchStoreAdapter = SearchStoreAdapter()

    val filterHashMap = HashMap<Int, String>()

    val tempCate = mutableListOf<OhneulenData>()

    //서버로 전송할 데이터
    val cate = mutableListOf<RequestBody>()
    val option = mutableListOf<String>()
    val openTime = mutableListOf<String>()
    val sort = mutableListOf<String>()


    //카테고리
    var mainCategoryPosition = MutableLiveData<Int>(0)
    var subCategoryPosition = 0

    val mainCategory = initData.mainCategory
    val subCategoryList = initData.subCategory
    var subCategory = MutableLiveData<List<OhneulenData>>(subCategoryList[0])

    //옵션
    var mainOptionKind = initData.mainOptionKind
    var subOptionKind = initData.subOptionKind

    //요일
    var timeDay = initData.timeDay


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
        }
    }

    fun filterSubmit() {
        CoroutineScope(Dispatchers.IO).launch {
            val cate1= mutableListOf<String>().apply {
                this.add("cate001001")
                this.add("cate003001")
            }
//            val cate1 = HashMap<String,MutableList<String>>().apply {
//                this["cate[]"]= mutableListOf()
//                this["cate[]"]!!.add("cate002001")
//            }

            try {
                val response = networkService.requestStoreSearchList(cate1)
                Timber.e(response.resultData.size.toString())

            } catch (e: Throwable) {
                Timber.e(e.toString())
            }
        }
    }


    val loading = MutableLiveData<Boolean>()

}