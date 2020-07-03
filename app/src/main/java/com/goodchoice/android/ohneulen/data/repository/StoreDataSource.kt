package com.goodchoice.android.ohneulen.data.repository

import androidx.paging.PageKeyedDataSource
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.service.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.ext.scope


class StoreDataSource(private val networkService: NetworkService) :
    PageKeyedDataSource<Int, Store>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Store>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                networkService.requestTestData(page = 1, pageSize = params.requestedLoadSize)
            val storeList = mutableListOf<Store>()
            for (i in response.news.indices) {
                storeList.add(
                    Store(
                        i.toString(), "한식",
                        "백반",
                        "한식$i",
                        response.news[i].title,
                        "주소", "도로명", 37.499417, 127.0250764, "open"
                    )
                )
            }
            callback.onResult(storeList, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Store>) {
        CoroutineScope(Dispatchers.IO).launch {

            val response = networkService.requestTestData(
                page = params.key,
                pageSize = params.requestedLoadSize
            )
            val storeList = mutableListOf<Store>()
            for (i in response.news.indices) {
                storeList.add(
                    Store(
                        i.toString(), "한식",
                        "백반",
                        "한식$i",
                        response.news[i].title,
                        "주소", "도로명", 37.499417, 127.0250764, "open"
                    )
                )
            }
            callback.onResult(storeList, params.key)

        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Store>) {
    }


}