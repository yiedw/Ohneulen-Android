package com.goodchoice.android.ohneulen.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.ui.adapter.FAQAdapter
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.data.model.*
import com.goodchoice.android.ohneulen.ui.mypage.MyPageGoodAdapter
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchStoreAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuDetailAdapter
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import com.goodchoice.android.ohneulen.ui.mypage.MyPageViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchFilterSubAdapter
import com.goodchoice.android.ohneulen.ui.store.StoreImageAdapter
import com.goodchoice.android.ohneulen.ui.store.StoreImageDetailAdapter
import com.goodchoice.android.ohneulen.util.constant.BaseUrl

//searchStore
@BindingAdapter("searchStoreAdapter", "searchStore")
fun setSearchStoreAdapter(
    recyclerView: RecyclerView,
    adapter: SearchStoreAdapter?,
    items: List<Store>?
) {
    recyclerView.adapter = adapter?.apply {
        submitList(items)
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

@BindingAdapter("storeMenuDetail", "storeMenuDetailIndex")
fun setStoreMenuDetail(recyclerView: RecyclerView, items: List<StoreMenu>?, index: Int) {
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
            setOnNextClickListener(object : StoreMenuDetailAdapter.OnNextClickListener {
                override fun onNextClick(pos: Int) {
                    recyclerView.scrollToPosition(pos)
                }

            })
//        notifyDataSetChanged()
        }

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
    Glide.with(imageView.context).load("${BaseUrl.Ohneulen}${image.photoURL}").centerCrop()
        .into(imageView)
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

@BindingAdapter( "FAQ","mypageViewModelFAQ")
fun setFAQ(recyclerView: RecyclerView, items: List<FAQ>?,viewModel:MyPageViewModel) {
    recyclerView.adapter = FAQAdapter().apply {
        if (items != null) {
            myPageViewModel=viewModel
            submitList(items)
        }
    }
}

@BindingAdapter("storeImageList")
fun setStoreImage(recyclerView: RecyclerView, items: List<Image>?) {
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.adapter = StoreImageAdapter().apply {
        if (items != null) {
            submitList(items)
        }
    }
}

@BindingAdapter("imageDetailList", "imageDetailIndex","imageDetailDialog")
fun setImageDetail(recyclerView: RecyclerView, items: List<Image>?, index: Int,dialog:DialogFragment) {
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    linearLayoutManager.orientation = RecyclerView.HORIZONTAL
    linearLayoutManager.scrollToPosition(index)
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.onFlingListener = null;
    //viewpager 처럼 딱딱 끊어지게
    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)

    recyclerView.adapter = StoreImageDetailAdapter()
        .apply {
            imageList = items ?: emptyList()
            dialogFragment=dialog
            setOnNextClickListener(object : StoreImageDetailAdapter.OnNextClickListener {
                override fun onNextClick(pos: Int) {
                    recyclerView.scrollToPosition(pos)
                }

            })
        }
}


