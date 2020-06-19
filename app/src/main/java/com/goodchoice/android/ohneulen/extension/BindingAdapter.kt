package com.goodchoice.android.ohneulen.extension

import android.Manifest
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.model.PartnerMenu
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.ui.partner.PartnerMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchFilterAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchFilterFragment
import com.goodchoice.android.ohneulen.ui.search.SearchPartnerAdapter
import com.goodchoice.android.ohneulen.util.ConstList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.daum.mf.map.api.MapView
import timber.log.Timber


@BindingAdapter("partner")
fun setSearchPartner(recyclerView: RecyclerView, items: List<Partner>?) {
    recyclerView.adapter = SearchPartnerAdapter().apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("partnerMenu")
fun setPartnerMenu(recyclerView: RecyclerView, items: List<PartnerMenu>?) {
    recyclerView.adapter = PartnerMenuAdapter().apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("mainCategory")
fun setMainCategory(recyclerView: RecyclerView, items: List<String>) {
    recyclerView.adapter = SearchFilterAdapter(ConstList.MAIN_CATEGORY).apply {
        itemList = items
        notifyDataSetChanged()
    }
}

@BindingAdapter("subCategory")
fun setSubCategory(recyclerView: RecyclerView, items: List<String>) {
    recyclerView.adapter = SearchFilterAdapter(ConstList.SUB_CATEGORY).apply {
        itemList = items
        notifyDataSetChanged()
    }
    (recyclerView.adapter as SearchFilterAdapter).subCategory.observe(
        SearchFilterFragment(),
        Observer {
            Timber.e((recyclerView.adapter as SearchFilterAdapter).subCategory.value)
        })
}



