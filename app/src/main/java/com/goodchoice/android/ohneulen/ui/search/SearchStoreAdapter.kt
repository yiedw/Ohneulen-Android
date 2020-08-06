package com.goodchoice.android.ohneulen.ui.search

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber


class SearchStoreAdapter :
    ListAdapter<Store, SearchStoreAdapter.SearchStoreViewHolder>(SearchStoreDiffUtil) {


    inner class SearchStoreViewHolder(private val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Store) {
            binding.apply {
                store = item
                //빈 하트일때
                storeItemGoodBasic.setOnClickListener {
                    if (!LoginViewModel.isLogin.value!!) {
                        val dialog = Dialog(root.context)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.logout_dialog)
                        dialog.findViewById<TextView>(R.id.logout_dialog_tv2).text =
                            root.context.getString(R.string.require_login)
                        dialog.findViewById<TextView>(R.id.logout_dialog_tv1).text = "알림"
                        dialog.findViewById<Button>(R.id.logout_dialog_cancel).setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.findViewById<Button>(R.id.logout_dialog_ok).setOnClickListener {
                            replaceAppbarFragment(LoginAppBar.newInstance(true,SearchAppBar.newInstance()))
                            addMainFragment(Login.newInstance(),true)
                            dialog.dismiss()

                        }
                        dialog.show()
                        return@setOnClickListener
                    }
                    storeItemGoodRed.visibility = View.VISIBLE
                    storeItemGoodBasic.visibility = View.GONE
                    Toast.makeText(
                        root.context,
                        "찜 목록에 저장되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //빨간하트일때
                storeItemGoodRed.setOnClickListener {
                    storeItemGoodRed.visibility = View.GONE
                    storeItemGoodBasic.visibility = View.VISIBLE
                }
                if (item.image.isNotEmpty()) {
                    Glide.with(root).load("${BaseUrl.Ohneulen}${item.image[0].photoURL}")
                        .centerCrop().into(storeItemImage)
                } else {
                    storeItemImage.setImageResource(0)
                }
                root.setOnClickListener {

                    StoreFragment.storeSeq = item.seq
                    replaceAppbarFragment(StoreAppBar.newInstance(), tag = "storeAppBar")
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStoreViewHolder {
        return DataBindingUtil.inflate<StoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_item,
            parent,
            false
        ).let {
            SearchStoreViewHolder(it)
        }
    }

    override fun getItemCount() = super.getItemCount()

    override fun onBindViewHolder(holder: SearchStoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

object SearchStoreDiffUtil : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }

}