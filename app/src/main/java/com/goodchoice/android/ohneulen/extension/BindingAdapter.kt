package com.goodchoice.android.ohneulen.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.ui.store.StoreMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchFilterAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchStoreAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreMenuDetailAdapter
import timber.log.Timber

@BindingAdapter("searchStoreAdapter")
fun setSearchStoreAdapter(recyclerView: RecyclerView, adapter: SearchStoreAdapter?) {
    recyclerView.adapter = adapter
}

@BindingAdapter("searchStore")
fun setSearchStore(recyclerView: RecyclerView, items: List<Store>?) {
    recyclerView.adapter = (recyclerView.adapter as SearchStoreAdapter).apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("filterAdapter")
fun setFilterAdapter(recyclerView: RecyclerView, adapter: SearchFilterAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter("categoryList")
fun setCategory(recyclerView: RecyclerView, items: MutableList<Category>) {
    recyclerView.adapter = (recyclerView.adapter as SearchFilterAdapter).apply {
        itemList = items
        notifyDataSetChanged()

    }
}

@BindingAdapter("searchFilterViewModel")
fun setSearchViewModel(recyclerView: RecyclerView, viewModel: SearchViewModel) {
    recyclerView.adapter = (recyclerView.adapter as SearchFilterAdapter).apply {
        searchViewModel = viewModel
    }
}

@BindingAdapter("storeMenu")
fun setStoreMenu(recyclerView: RecyclerView, items: List<StoreMenu>?) {
    recyclerView.adapter = StoreMenuAdapter().apply {
        Timber.e("asdfsdaf" + items.toString())
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("storeMenuDetailAdapter")
fun setStoreMenuDetailAdapter(recyclerView: RecyclerView, adapter: StoreMenuDetailAdapter) {
    recyclerView.adapter = adapter
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    recyclerView.layoutManager = linearLayoutManager
    //viewpager 처럼 딱딱 끊어지게
    val snapHelper=PagerSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)
}

@BindingAdapter("storeMenuDetail")
fun setStoreMenuDetail(recyclerView: RecyclerView, items: List<StoreMenu>?) {
    Timber.e(items.toString())
    recyclerView.adapter = StoreMenuDetailAdapter().apply {
        menuList = items ?: emptyList()
        notifyDataSetChanged()
    }
}


