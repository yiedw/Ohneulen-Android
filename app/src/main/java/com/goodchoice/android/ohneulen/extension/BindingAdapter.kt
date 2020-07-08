package com.goodchoice.android.ohneulen.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.ui.mypage.MyPageGoodAdapter
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchFilterAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchStoreAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuDetailAdapter
import com.goodchoice.android.ohneulen.adapter.ReviewAdapter
import timber.log.Timber

//searchStore
@BindingAdapter("searchStoreAdapter", "searchStore")
fun setSearchStoreAdapter(
    recyclerView: RecyclerView,
    adapter: SearchStoreAdapter?,
    items: List<Store>?
) {
    recyclerView.adapter = adapter?.apply {
        itemList = items ?: emptyList()
//        notifyDataSetChanged()
    }

}

//filter
@BindingAdapter("filterAdapter", "categoryList", "searchFilterViewModel")
fun setFilterAdapter(
    recyclerView: RecyclerView,
    adapter: SearchFilterAdapter,
    items: MutableList<Category>,
    viewModel: SearchViewModel
) {
    recyclerView.adapter = adapter.apply {
        searchViewModel = viewModel
        itemList = items
//        notifyDataSetChanged()
    }
}


@BindingAdapter("storeMenu")
fun setStoreMenu(recyclerView: RecyclerView, items: List<StoreMenu>?) {
//    Timber.e(items.toString())
    recyclerView.adapter = StoreMenuAdapter()
        .apply {
        itemList = items ?: emptyList()
//        notifyDataSetChanged()
    }
}

@BindingAdapter("storeMenuDetail", "storeMenuDetailIndex")
fun setStoreMenuDetail(recyclerView: RecyclerView, items: List<StoreMenu>?, index: Int) {
    recyclerView.adapter = StoreMenuDetailAdapter()
        .apply {
        menuList = items ?: emptyList()
        notifyDataSetChanged()
    }
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    linearLayoutManager.scrollToPosition(index)
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.onFlingListener = null;
    //viewpager 처럼 딱딱 끊어지게
    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)
}

@BindingAdapter("imageResURL")
fun setImageViewURL(imageView: ImageView, resURL: String) {

}

@BindingAdapter("imageResID")
fun setImageViewResID(imageView: ImageView, resID: Int) {
    Glide.with(imageView.context)
        .load(resID)
        .centerCrop()
        .into(imageView)
}

@BindingAdapter("mypageGoodAdapter")
fun setMyPageGoodAdapter(recyclerView: RecyclerView, adapter: MyPageGoodAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter("reviewAdapter", "review")
fun setReview(recyclerView: RecyclerView, adapter: ReviewAdapter, items: List<Review>?) {
    recyclerView.adapter=adapter.apply {
        if (items != null) {
            reviewList=items
//            notifyDataSetChanged()
        }
    }

}

@BindingAdapter("inquireAdapter","inquire")
fun setInquire(recyclerView: RecyclerView,adapter:InquireAdapter,items: List<Inquire>?){
    recyclerView.adapter=adapter.apply {
        if(items!=null){
            inquireList=items
        }
    }
}

