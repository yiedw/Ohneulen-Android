package com.goodchoice.android.ohneulen.data.repository

import androidx.lifecycle.MutableLiveData
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.getOhneulenData
import com.goodchoice.android.ohneulen.util.getOhneulenSubData
import kotlinx.coroutines.*
import timber.log.Timber

class InitData(private val networkService: NetworkService) {


    companion object {
        var endNumber = MutableLiveData(0)
    }

    //카테고리
    var mainCategory = mutableListOf<OhneulenData>()
    var subCategoryList = mutableListOf<List<OhneulenData>>()
    var subCategory = MutableLiveData<List<OhneulenData>>()

    //옵션
    var mainOptionKind = mutableListOf<OhneulenData>()
    var subOptionKind = mutableListOf<List<OhneulenData>>()

    //요일
    var timeDay = mutableListOf<OhneulenData>()

    private var checkInitData = 0


    //옵션

    init {
        //옵션 가져오기
        getOptionKind()
        //카테고리 가져오기
        getCategory()
        //요일 가져오기
        getTimeDay()

    }

    private fun getCategory() {
        CoroutineScope(Dispatchers.IO).launch {
            mainCategory = getOhneulenData(networkService, ConstList.CATEGORY)
            for (i in mainCategory.indices) {
                val tempList = getOhneulenData(networkService, mainCategory[i].minorCode)
                subCategoryList.add(tempList)
            }
            Timber.e(subCategoryList.toString())
            subCategory.postValue(subCategoryList[0])
//            subCategoryList = getOhneulenSubData(networkService, mainCategory)
            checkInitData++
            endNumber.postValue(checkInitData)
        }
    }

    private fun getOptionKind() {
        CoroutineScope(Dispatchers.IO).launch {
            mainOptionKind = getOhneulenData(networkService, ConstList.OPTION_KIND)
//            for (i in mainOptionKind.indices) {
//                val tempList = getOhneulenData(networkService, mainOptionKind[i].minorCode)
//                subOptionKind.add(tempList)
//            }
            subOptionKind = getOhneulenSubData(networkService, mainOptionKind)
            checkInitData++
            endNumber.postValue(checkInitData)
        }
    }

    private fun getTimeDay() {
        CoroutineScope(Dispatchers.IO).launch {
            timeDay = getOhneulenData(networkService, ConstList.TIME_DAY)
            checkInitData++
            endNumber.postValue(checkInitData)
        }
    }

}