package com.goodchoice.android.ohneulen.ui.store

import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.util.constant.ConstList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class StoreViewModel(private val networkService: NetworkService) : ViewModel() {


    //network resultCode
    val networkResultCode = MutableLiveData<String>()

    //storeDetail 가져오기
    var storeDetail = MutableLiveData<StoreDetail>()
    var storeMenuList = listOf<StoreMenu>()
    var storeReviewList = listOf<Review>()
    var storeSeq = StoreFragment.storeSeq
    fun getStoreDetail(storeSeq: String) {
        this.storeSeq = storeSeq
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val storeDetailResponse = networkService.requestGetStoreInfo(
                    storeSeq.toRequestBody()
                )
                if (storeDetail.value == storeDetailResponse.resultData)
                    return@launch
                if (storeDetailResponse.resultCode == "000") {
                    if (storeDetail.value != storeDetailResponse.resultData) {
                        storeDetail.postValue(storeDetailResponse.resultData)
                        storeMenuList = storeDetailResponse.resultData.menuList
                        //최신순부터 보여주기위해 리스트를 한번 뒤집음  (서버단에서 데이터 수정해서 필요 없음)
//                        storeDetailResponse.resultData.reviewList = storeDetailResponse.resultData.reviewList.reversed()
                        storeReviewList = storeDetailResponse.resultData.reviewList
                    }
                }

            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
    }

    var storeReviewHeightCheck = false


    //로그인 했을때 찜 정보만 받아오기
    fun getStoreFavoriteCheck(storeSeq: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val storeDetailResponse = networkService.requestGetStoreInfo(
                    storeSeq.toRequestBody()
                )
                if (storeDetail.value == storeDetailResponse.resultData)
                    return@launch
                if (storeDetailResponse.resultCode == "000") {
                    if (storeDetail.value != storeDetailResponse.resultData) {
                        storeFavoriteCheckLiveData.postValue(storeDetailResponse.resultData.storeInfo.storeFull.likes)
                    }
                }

            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
    }

    var storeFavoriteCheckLiveData = MutableLiveData<Boolean>(false)

    //찜 설정
    var setMemberLikeResponseCode = MutableLiveData<String>()
    fun setMemberLike() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkService.requestSetMemberLike(StoreFragment.storeSeq)
                setMemberLikeResponseCode.postValue(response.resultCode)
            } catch (e: Exception) {
                Timber.e("찜에 실패하였습니다.")
            }
        }
    }

    var storeLikeCntLiveData = MutableLiveData<Int>(0)

    //스토어화면에서 뒤로갔을때 서치 리스트로 데이터를 보존하기 위함
    var storeFavoriteIsChecked = false
    var storePoint = 0.0
    var storeLikeCnt = 0
    var storeReviewCnt = 0


    //menuDetail 클릭했을때 클릭한 곳으로 이동
    var menuIndex = 0
    var menuDetailListSize = 0


    //storeimage
    var storeImageDetailIndex = 0


    //후기 신고할때
    val date = Calendar.getInstance().time
    val today = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(date)

    //후기 작성
    var setReviewCode = MutableLiveData<String>()
    val reviewImgList = mutableListOf<String>()
    fun setReview(
        point0: String,
        reviewSelect01: String,
        reviewSelect02: String,
        reviewSelect03: String,
        reviewSelect04: String,
        reviewSelect05: String,
        reviewText: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkService.requestSetReview(
                    storeSeq,
                    point0,
                    reviewSelect01,
                    reviewSelect02,
                    reviewSelect03,
                    reviewSelect04,
                    reviewSelect05,
                    reviewText,
                    reviewImgList
                )
                setReviewCode.postValue(response.resultCode)


            } catch (e: Exception) {
                Timber.e(e.toString())
            }
        }
    }

    var toastMessageCheck = MutableLiveData("000")
    fun imageUpload(file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val response = networkService.requestImageUpload(body)
                if (response.resultCode == ConstList.SUCCESS) {
                    reviewImgList.add("/public/upload/storeimg/${response.resultData.file_name}")
                }
//                else if(response.resultCode=="321"){
//                    toastMessageCheck.postValue("321")
//                }
            } catch (e: Exception) {
                Timber.e(e.toString())
            }
        }
    }


    fun storeReport(gubun1: String, contents: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val response = networkService.requestSetBoard(
                    ConstList.STORE_REPORT,
                    gubun1, "", contents
                )
                networkResultCode.postValue(response.resultCode)

            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
    }


    val loading = MutableLiveData<Boolean>()


}