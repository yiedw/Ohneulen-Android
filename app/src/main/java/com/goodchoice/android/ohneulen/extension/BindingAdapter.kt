package com.goodchoice.android.ohneulen.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.ui.store.StoreMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchFilterAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchStoreAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel

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
fun setFilterAdapter(recyclerView: RecyclerView,adapter:SearchFilterAdapter){
    recyclerView.adapter=adapter
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
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("imageView")
fun setImageView(imageView:ImageView,imageUrl:String){
    Glide.with(imageView.context)
        .load(imageUrl)
        .override(imageView.width,imageView.height)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(imageView)
}


