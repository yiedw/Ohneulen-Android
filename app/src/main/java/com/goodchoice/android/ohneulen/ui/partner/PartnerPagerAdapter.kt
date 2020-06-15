package com.goodchoice.android.ohneulen.ui.partner

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PartnerPagerAdapter(
    private val arrayList:ArrayList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = arrayList.size

    override fun createFragment(position: Int): Fragment {
        return arrayList[position]
    }

    fun getViewAtPosition(position:Int): View? {
        return arrayList[position].view
    }


}