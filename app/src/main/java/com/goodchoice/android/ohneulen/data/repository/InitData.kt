package com.goodchoice.android.ohneulen.data.repository

import androidx.lifecycle.MutableLiveData
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.getOhneulenData
import com.goodchoice.android.ohneulen.util.getOhneulenSubData
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

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
        //카테고리 가져오기
        getCategory()
        //옵션 가져오기
        getOptionKind()
        //요일 가져오기
        getTimeDay()

    }

    private fun getCategory() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val mainCategoryScope = CoroutineScope(Dispatchers.IO).launch {
                    mainCategory = getOhneulenData(networkService, ConstList.CATEGORY)
                }
                mainCategoryScope.join()

                for (i in mainCategory.indices) {
                    val tempList = getOhneulenData(networkService, mainCategory[i].minorCode)
                    subCategoryList.add(tempList)
                    if (i == 0) {
                        Timber.e(subCategoryList.toString())
                        subCategory.postValue(subCategoryList[0])
                    }
                }
//            subCategoryList = getOhneulenSubData(networkService, mainCategory)
                checkInitData++
                endNumber.postValue(checkInitData)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getOptionKind() {
        try {
            CoroutineScope(Dispatchers.IO).launch {

                val mainOptionKindScope = CoroutineScope(Dispatchers.IO).launch {
                    mainOptionKind = getOhneulenData(networkService, ConstList.OPTION_KIND)
//            for (i in mainOptionKind.indices) {
//                val tempList = getOhneulenData(networkService, mainOptionKind[i].minorCode)
//                subOptionKind.add(tempList)
//            }
                }
                mainOptionKindScope.join()
                subOptionKind = getOhneulenSubData(networkService, mainOptionKind)
                checkInitData++
                endNumber.postValue(checkInitData)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getTimeDay() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                timeDay = getOhneulenData(networkService, ConstList.TIME_DAY)
                checkInitData++
                endNumber.postValue(checkInitData)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

}