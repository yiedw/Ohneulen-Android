package com.goodchoice.android.ohneulen.extension

import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.adapter.FAQAdapter
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import com.goodchoice.android.ohneulen.ui.mypage.MyPageGoodAdapter
import com.goodchoice.android.ohneulen.ui.mypage.MyPageViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchFilterSubAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchStoreAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.ui.adapter.ImageDetailAdapter
import com.goodchoice.android.ohneulen.ui.store.StoreImageAdapter
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuAdapter
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuDetailAdapter
import com.goodchoice.android.ohneulen.util.constant.BaseUrl

//searchStore
@BindingAdapter("searchStoreAdapter", "searchStore","searchStoreNetworkService")
fun setSearchStoreAdapter(
    recyclerView: RecyclerView,
    adapter: SearchStoreAdapter?,
    items: List<Store>?,
    networkService: NetworkService
) {

    val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
    smoothScroller.targetPosition = 0
    recyclerView.setHasFixedSize(true)
    recyclerView.adapter = adapter?.apply {
        parentView = recyclerView
        mNetworkService=networkService
        submitList(items)
        Handler().postDelayed({
            recyclerView.layoutManager!!.startSmoothScroll(smoothScroller)
        }, 200)

    }


}

//filter
@BindingAdapter("subCategory", "subCategoryViewModel")
fun setFilterSubAdapter(
    recyclerView: RecyclerView,
    items: List<OhneulenData>,
    viewModel: SearchViewModel
) {
    recyclerView.adapter = SearchFilterSubAdapter().apply {
        searchViewModel = viewModel
        submitList(items)
    }
}


@BindingAdapter("storeMenu")
fun setStoreMenu(recyclerView: RecyclerView, items: List<StoreMenu>?) {
//    Timber.e(items.toString())
    recyclerView.adapter = StoreMenuAdapter()
        .apply {
            submitList(items)
        }
}

@BindingAdapter("storeMenuDetail", "storeMenuDetailIndex", "storeMenuDetailLoading")
fun setStoreMenuDetail(
    recyclerView: RecyclerView,
    items: List<StoreMenu>?,
    index: Int,
    loading: MutableLiveData<Boolean>
) {
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    linearLayoutManager.scrollToPosition(index)
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.onFlingListener = null;
    //viewpager 처럼 딱딱 끊어지게
    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)

    recyclerView.adapter = StoreMenuDetailAdapter()
        .apply {
            menuList = items ?: emptyList()
            menuPosition.postValue(index)
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val pos: Int? =
                            centerView?.let { recyclerView.layoutManager!!.getPosition(it) }
                        menuPosition.postValue(pos)
                    }
                }
            })
        }
    loading.postValue(true)

}

@BindingAdapter("imageResURL")
fun setImageViewURL(imageView: ImageView, resURL: String) {
    Glide.with(imageView.context).load(resURL).centerCrop().into(imageView)
}

@BindingAdapter("ohneulenImage")
fun setOhneulenImageView(
    imageView: ImageView,
    image: Image
) {
//    Timber.e(image.photoURL)
    Glide.with(imageView.context).load("${BaseUrl.OHNEULEN}${image.photoURL}")
        .into(imageView)
}

@BindingAdapter("ohneulenImageURL")
fun setOhneulenURLImageView(
    imageView: ImageView,
    imageUrl: String?
) {
    if (imageUrl != null)
        Glide.with(imageView.context).load("${BaseUrl.OHNEULEN}${imageUrl}").centerCrop()
            .into(imageView)
    else
        Glide.with(imageView.context)
            .clear(imageView)
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
    recyclerView.adapter = adapter.apply {
        if (items != null) {
            submitList(items)
        }
    }

}

@BindingAdapter("inquireAdapter", "inquire")
fun setInquire(recyclerView: RecyclerView, adapter: InquireAdapter, items: List<Inquire>?) {
    recyclerView.adapter = adapter.apply {
        if (items != null) {
            submitList(items)
        }
    }
}

@BindingAdapter("FAQ", "mypageViewModelFAQ")
fun setFAQ(recyclerView: RecyclerView, items: List<FAQ>?, viewModel: MyPageViewModel) {
    recyclerView.adapter = FAQAdapter().apply {
        if (items != null) {
            myPageViewModel = viewModel
            submitList(items)
        }
    }
}

@BindingAdapter("storeImageList")
fun setStoreImage(recyclerView: RecyclerView, items: List<Image>?) {
    recyclerView.onFlingListener = null
    val snapHelper = LinearSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.adapter = StoreImageAdapter().apply {
        if (items != null) {
            submitList(items)
        }
    }
}

@BindingAdapter("imageDetailList", "imageDetailIndex", "imageDetailLoading")
fun setImageDetailStore(
    recyclerView: RecyclerView,
    items: List<Image>?,
    index: Int = 0,
    loading: MutableLiveData<Boolean>
) {
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    linearLayoutManager.scrollToPosition(index)

    recyclerView.layoutManager = linearLayoutManager
    recyclerView.onFlingListener = null;
    //viewpager 처럼 딱딱 끊어지게
    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)

    recyclerView.adapter = ImageDetailAdapter()
        .apply {
            imageList = items ?: emptyList()
            imagePosition.postValue(index)

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val pos: Int? =
                            centerView?.let { recyclerView.layoutManager!!.getPosition(it) }
                        imagePosition.postValue(pos)

                    }

                }
            })
        }
    loading.postValue(true)

}


