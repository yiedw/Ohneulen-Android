package com.goodchoice.android.ohneulen.ui.store.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.databinding.StoreMenuDetailBinding
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreMenuDetailDialog(private var inputIndex: Int) : DialogFragment(), OnBackPressedListener {

    companion object {
        fun newInstance(index: Int) =
            StoreMenuDetailDialog(
                index
            )
    }

    private lateinit var binding: StoreMenuDetailBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private var menuPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.image_dialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storeViewModel.menuIndex = this.inputIndex
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_menu_detail,
            null,
            false
        )
        binding.viewModel = storeViewModel
        //this로 하면 무한로딩에 걸림
        binding.lifecycleOwner = binding.lifecycleOwner
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                val list = mutableListOf<StoreMenu>()
                for (i in storeViewModel.storeMenuList) {
                    if (i.photoURL.isNotEmpty()) {
                        list.add(i)
                    }
                }
                (binding.storeMenuDetailRv.adapter as StoreMenuDetailAdapter).menuList = list


                (binding.storeMenuDetailRv.adapter as StoreMenuDetailAdapter).menuPosition.observe(
                    viewLifecycleOwner,
                    Observer { pos ->
                        menuPosition = pos
                        if (pos == 0) {
                            binding.storeMenuDetailLeft.visibility = View.GONE
                        } else {
                            binding.storeMenuDetailLeft.visibility = View.VISIBLE
                        }
                        if (pos == (binding.storeMenuDetailRv.adapter as StoreMenuDetailAdapter).menuList.size - 1) {
                            binding.storeMenuDetailRight.visibility = View.GONE
                        } else {
                            binding.storeMenuDetailRight.visibility = View.VISIBLE
                        }
                    }
                )
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        storeViewModel.loading.postValue(false)
    }

    fun onLeftClick(view: View) {
        binding.storeMenuDetailRv.smoothScrollToPosition(menuPosition - 1)
    }

    fun onRightClick(view: View) {
        binding.storeMenuDetailRv.smoothScrollToPosition(menuPosition + 1)
    }

    fun onBackClick(view: View) {
        dismiss()
    }

    override fun onBackPressed() {
        dismiss()
    }


}