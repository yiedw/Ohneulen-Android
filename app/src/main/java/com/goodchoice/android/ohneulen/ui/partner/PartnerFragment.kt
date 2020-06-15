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
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.internal.notify
import timber.log.Timber
import java.text.FieldPosition

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
    private val initConstraintLayout: ViewGroup.LayoutParams =
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
        }
    }


    private fun viewPagerSetting() {
        binding.partnerViewPager2.adapter = PartnerPagerAdapter(
            getFragmentList(), childFragmentManager,
            lifecycle
        )
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
                }
            }
        )
    }

    fun menuClick(view: View) {
        elseClickSetting()
        val childFragment = childFragmentManager
        childFragment.beginTransaction().replace(
            R.id.partner_frameLayout,
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

    private fun getFragmentList(): ArrayList<Fragment> {
        return arrayListOf<Fragment>(
            PartnerHomeFragment.newInstance(),
            PartnerMapFragment.newInstance(),
            PartnerMenuFragment.newInstance(),
            PartnerReviewFragment.newInstance()
        )
    }

    private fun basicSetting() {
        MainActivity.appbarFrameLayout.background =
            ContextCompat.getDrawable(requireActivity(), R.color.colorTransparent)
        binding.partnerNewScrollView.scrollTo(0, 0)
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

    private fun reviewSetting() {
        binding.partnerNewScrollView.scrollTo(0, 0)
        binding.partnerImage.visibility = View.GONE
        MainActivity.mainFrameLayout.layoutParams = initConstraintLayout
    }


}