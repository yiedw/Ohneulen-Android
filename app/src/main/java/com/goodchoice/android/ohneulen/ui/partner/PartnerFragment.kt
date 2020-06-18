package com.goodchoice.android.ohneulen.ui.partner

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
import com.goodchoice.android.ohneulen.databinding.PartnerFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PartnerFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerFragment()

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

    private lateinit var binding: PartnerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.partner_fragment,
            container,
            false
        )
        binding.fragment = this
        //어둡게 만들기
        binding.partnerBigImage.setColorFilter(
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

    //스크롤되면 헤더 붙이기
    private fun stickyHeader() {
        binding.partnerTab.bringToFront()
        binding.partnerNewScrollView.run {
            header = binding.partnerTab
            freeHeader()
        }
    }


    //viewPager setting
    private fun viewPagerSetting() {
        binding.partnerViewPager2.adapter = PartnerPagerAdapter(
            getFragmentList(), childFragmentManager,
            lifecycle
        )
        binding.partnerViewPager2.offscreenPageLimit = 4

        //애니메이션 삭제
        binding.partnerTab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    binding.partnerViewPager2.setCurrentItem(it,false)
                }
            }

        })


        //탭 연결
        val tabLayoutTextList = mutableListOf("홈", "지도", "메뉴", "후기")
        TabLayoutMediator(binding.partnerTab, binding.partnerViewPager2) { tab, position ->
            tab.text = tabLayoutTextList[position]
        }.attach()
        binding.partnerViewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    state = position
                    if (position == 3) {
                        reviewSetting()
                    } else {
                        basicSetting()
                    }
                    val view =
                        (binding.partnerViewPager2.adapter as PartnerPagerAdapter).getViewAtPosition(
                            position
                        )
                    updatePagerHeightForChild(view!!, binding.partnerViewPager2)
                    binding.partnerNewScrollView.scrollTo(0, 0)
                    stickyHeader()
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
            PartnerHomeFragment.newInstance(),
            PartnerMapFragment.newInstance(),
            PartnerMenuFragment.newInstance(),
            PartnerReviewFragment.newInstance()
        )
    }

    //기본세팅
    //mainFragment -> Appbar 와 겹치게
    private fun basicSetting() {
        MainActivity.appbarFrameLayout.background =
            ContextCompat.getDrawable(requireActivity(), R.color.colorTransparent)
        binding.partnerImage.visibility = View.VISIBLE
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            0
        )
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        MainActivity.mainFrameLayout.layoutParams = layoutParams
    }

    //리뷰 페이지 세팅
    private fun reviewSetting() {
        binding.partnerImage.visibility = View.GONE
        MainActivity.mainFrameLayout.layoutParams = initMainFragment

    }


}