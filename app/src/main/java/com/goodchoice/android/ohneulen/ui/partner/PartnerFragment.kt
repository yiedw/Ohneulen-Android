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
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerFragmentBinding

class PartnerFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerFragment()
        //1 -> 나머지 클릭
        //2 -> 리뷰클릭
        var state = 1

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

        //레이아웃 위치 재조정
//        elseClickSetting()

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
        homeClick(view)
        stickyHeader()
    }

    //스크롤되면 헤더 붙이기
    private fun stickyHeader() {
        binding.partnerInfo.bringToFront()
        binding.partnerNewScrollView.run {
            header = binding.partnerInfo
        }
    }

    fun homeClick(view: View) {
        elseClickSetting()
        val childFragment = childFragmentManager
        childFragment.beginTransaction().replace(
            R.id.partner_frameLayout,
            PartnerHomeFragment.newInstance()
        ).commitNow()
    }

    fun mapClick(view: View) {
        elseClickSetting()
        val childFragment = childFragmentManager
        childFragment.beginTransaction().replace(
            R.id.partner_frameLayout,
            PartnerMapFragment.newInstance()
        ).commitNow()
    }

    fun menuClick(view: View) {
        elseClickSetting()
        val childFragment = childFragmentManager
        childFragment.beginTransaction().replace(
            R.id.partner_frameLayout,
            PartnerMenuFragment.newInstance()
        ).commitNow()
    }

    fun reviewClick(view: View) {
        reviewClickSetting()
        val childFragment = childFragmentManager
        childFragment.beginTransaction().replace(
            R.id.partner_frameLayout,
            PartnerReviewFragment.newInstance()
        ).commitNow()

    }

    private fun elseClickSetting() {
        MainActivity.appbarFrameLayout.background=
            ContextCompat.getDrawable(requireActivity(),R.color.colorTransparent)
        state=1
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

    private fun reviewClickSetting() {
        state=2
        binding.partnerNewScrollView.scrollTo(0, 0)
        binding.partnerImage.visibility = View.GONE
        MainActivity.mainFrameLayout.layoutParams = initConstraintLayout
    }


}