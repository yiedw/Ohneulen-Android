package com.goodchoice.android.ohneulen.ui.store

import android.widget.Toast
import androidx.lifecycle.*
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class StoreViewModel(private val networkService: NetworkService) : ViewModel() {

    //storeDetail 가져오기
    var storeMenuList = listOf<StoreMenu>()
    var storeDetail = MutableLiveData<StoreDetail>()
    fun getStoreDetail(storeSeq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val storeDetailResponse = networkService.requestGetStoreInfo(
                storeSeq.toRequestBody()
            )
            if (storeDetailResponse.resultCode == "000") {
                storeDetail.postValue(storeDetailResponse.resultData)
                storeMenuList = storeDetailResponse.resultData.menuList
            }

        }
    }

    //찜 설정
    var setMemberLikeResponseCode = MutableLiveData<String>()
    var storeLike=MutableLiveData<Boolean>(false)
    fun setMemberLike() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkService.requestSetMemberLike(StoreFragment.storeSeq)
                setMemberLikeResponseCode.postValue(response.resultCode)
                Timber.e(setMemberLikeResponseCode.value!!)
            } catch (e: Exception) {
                Timber.e("찜에 실패하였습니다.")
            }
        }
    }


    //menuDetail 클릭했을때 클릭한 곳으로 이동
    var menuIndex = 0


    //storeimage
    var storeImageDetailIndex = 0
    var storeReviewAdapter = ReviewAdapter()


    //후기 신고할때
    val date = Calendar.getInstance().time
    val today = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(date)

    //storeReviewImage


    val loading = MutableLiveData<Boolean>()


}