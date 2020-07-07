package com.goodchoice.android.ohneulen.ui.store

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreFragmentBinding
import com.goodchoice.android.ohneulen.ui.store.home.StoreHomeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreFragment : Fragment() {

    companion object {
        fun newInstance() = StoreFragment()

        // 각 fragment
        // 0 -> home
        // 1 -> map
        // 2 -> menu
        // 3 -> review
        var state = 0

    }

    //나중에 되돌리기
    private val initMainFragment: ViewGroup.LayoutParams =
        MainActivity.mainFrameLayout.layoutParams

    private lateinit var binding: StoreFragmentBinding
    private val storeViewModel:StoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_fragment,
            container,
            false
        )
        binding.fragment = this
        binding.viewModel=storeViewModel
        //어둡게 만들기
        binding.storeBigImage.setColorFilter(
            ContextCompat.getColor(requireActivity(), R.color.colorTransparentBlack),
            PorterDuff.Mode.SRC_OVER
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        viewPagerSetting()
        stickyHeader()

    }

    override fun onResume() {
        super.onResume()
    }


    //스크롤되면 헤더 붙이기
    private fun stickyHeader() {
        binding.storeTab.bringToFront()
        binding.storeNewScrollView.run {
            header = binding.storeTab
            freeHeader()
        }
    }


    //viewPager setting
    private fun viewPagerSetting() {
        binding.storeViewPager2.adapter = StorePagerAdapter(
            getFragmentList(), childFragmentManager,
            lifecycle
        )
        binding.storeViewPager2.offscreenPageLimit = 4

        //애니메이션 삭제
        binding.storeTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    binding.storeViewPager2.setCurrentItem(it, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    binding.storeViewPager2.setCurrentItem(it, true)
                }
            }

        })


        //탭 연결
        val tabLayoutTextList = mutableListOf("홈", "지도", "메뉴", "후기")
        TabLayoutMediator(binding.storeTab, binding.storeViewPager2) { tab, position ->
            tab.text = tabLayoutTextList[position]
        }.attach()
        binding.storeViewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    state = position
                    if (position == 3) {
                        reviewSetting()
                    } else if (position == 1) {
                        basicSetting()
                        mapSetting()
//                        scrollBlock()
                    } else {
                        basicSetting()
                    }
                    val view =
                        (binding.storeViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                            position
                        )
                    updatePagerHeightForChild(view!!, binding.storeViewPager2)
                    binding.storeNewScrollView.scrollTo(0, 0)
                    stickyHeader()
                    binding.storeNewScrollView.invalidate()
                }
            }
        )
    }

    //viewPager2 크기조절
    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
        }
    }

    //viewPager에 들어갈 fragmentList
    private fun getFragmentList(): ArrayList<Fragment> {
        return arrayListOf(
            StoreHomeFragment.newInstance(),
            StoreMapFragment.newInstance(),
            StoreMenuFragment.newInstance(),
            StoreReviewFragment.newInstance()
        )
    }

    //기본세팅
    //mainFragment -> Appbar 와 겹치게
    private fun basicSetting() {
        MainActivity.appbarFrameLayout.background =
            ContextCompat.getDrawable(requireActivity(), R.color.colorTransparent)
        binding.storeImage.visibility = View.VISIBLE
        //mainfragment 를 화면 맨위에 딱 붙이게 하기위한 작업
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            0
        )
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        MainActivity.mainFrameLayout.layoutParams = layoutParams
    }

    private fun mapSetting(){
        //지도를 화면에 딱맞게(스크롤뷰 안먹게)
        val layoutParams=ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            0
        )
        layoutParams.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToBottom=R.id.store_tab
        layoutParams.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID
        binding.storeViewPager2.layoutParams=layoutParams
    }

    //리뷰 페이지 세팅
    private fun reviewSetting() {
        binding.storeImage.visibility = View.GONE
        MainActivity.mainFrameLayout.layoutParams = initMainFragment

        //리뷰가 없을때 후기가 위로 딱 붙게하기
        val layoutParams=ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToBottom=R.id.store_tab
        binding.storeViewPager2.layoutParams=layoutParams

    }


}