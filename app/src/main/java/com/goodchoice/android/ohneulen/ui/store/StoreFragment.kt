package com.goodchoice.android.ohneulen.ui.store

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreFragmentBinding
import com.goodchoice.android.ohneulen.ui.store.home.StoreHome
import com.goodchoice.android.ohneulen.ui.store.map.StoreMap
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenu
import com.goodchoice.android.ohneulen.ui.store.review.StoreReview
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.dp
import com.goodchoice.android.ohneulen.util.textColor
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.store_home.view.*
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreFragment : Fragment() {

    companion object {
        fun newInstance() = StoreFragment()
        var storeSeq: String = ""
//        lateinit var store: Store

        // 각 fragment
        // 0 -> home
        // 1 -> map
        // 2 -> menu
        // 3 -> review
        var state = 0

    }

    private lateinit var binding: StoreFragmentBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.GONE
    }

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
        binding.viewModel = storeViewModel
        binding.fragment = this
        binding.lifecycleOwner = this

        storeViewModel.getStoreDetail(storeSeq)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            if (it.storeInfo.image.size == 1) {
                binding.storeFragmentOneImage.visibility = View.VISIBLE
                binding.storeFragmentImageRv.visibility = View.INVISIBLE
                Glide.with(requireContext())
                    .load("${BaseUrl.Ohneulen}${it.storeInfo.image[0].photoURL}")
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20)))
                    .into(binding.storeFragmentOneImage)
            } else {
                binding.storeFragmentOneImage.visibility = View.GONE
                binding.storeFragmentImageRv.visibility = View.VISIBLE
            }
            storeHeader(it)
            binding.storeNewScrollView.scrollTo(0, 0)

            if(it.menuList.isNullOrEmpty()){
                binding.storeTab.removeTabAt(2)
            }
        })

        viewPagerSetting()
        stickyHeader()
        binding.storeNewScrollView.scrollTo(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }


    //카테고리 좋아요 후기 갯수
    private fun storeHeader(storeDetail: StoreDetail) {
        val store = storeDetail.storeInfo.store
        val likeCnt =
            textColor("51", 0, 2, ContextCompat.getColor(requireContext(), R.color.colorOhneulen))
        val reviewCnt = textColor(
            storeDetail.reviewCnt.toString(),
            0,
            storeDetail.reviewCnt.toString().length,
            ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
        )

        val text =
            TextUtils.concat("${store.cate1Name!!.minorName} /좋아요 ", likeCnt, " /후기 ", reviewCnt)
        binding.storeFragmentDetail.text = text
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
        binding.storeViewPager2.offscreenPageLimit = getFragmentList().size

        //애니메이션 삭제
//        binding.storeTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                tab?.position?.let {
//                    binding.storeViewPager2.setCurrentItem(it, true)
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                tab?.position?.let {
//                    binding.storeViewPager2.setCurrentItem(it, true)
//                }
//            }
//
//        })


        //탭 연결
        val tabLayoutTextList = mutableListOf("홈", "지도", "메뉴", "후기")
        TabLayoutMediator(binding.storeTab, binding.storeViewPager2) { tab, position ->
            tab.text = tabLayoutTextList[position]
        }.attach()
        binding.storeNewScrollView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.storeViewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    state = position

                    val view =
                        (binding.storeViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                            position
                        )
                    view?.let {
                        if (position == 1) {
                            mapSetting()
//                            binding.storeFragmentImageRv.visibility = View.VISIBLE
                        }
                        else {
                            updatePagerHeightForChild(view, binding.storeViewPager2)
//                            binding.storeFragmentImageRv.visibility = View.VISIBLE

                        }
                    }

                    binding.storeNewScrollView.scrollTo(0, 0)
//                    stickyHeader()
//                    if (position == 1) {
//                    }
//                    binding.storeNewScrollView.invalidate()
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
            StoreHome.newInstance(),
            StoreMap.newInstance(),
            StoreMenu.newInstance(),
            StoreReview.newInstance()
        )
    }


    private fun mapSetting() {
        //지도를 화면에 딱맞게(스크롤뷰 안먹게)
        val metrics = resources.displayMetrics
        //store_map_nav height만큼 다시 빼준다
        val px = 48 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            MainActivity.bottomNav.bottom - binding.storeTab.bottom - px
//            MainActivity.bottomNav.top - binding.storeTab.bottom - px
        )
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToBottom = R.id.store_tab
//        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        binding.storeViewPager2.layoutParams = layoutParams
    }

    fun oneImageClick(view:View){
        val dialog=StoreImageDetailDialog.newInstance(0)
        dialog.show(MainActivity.supportFragmentManager,"")
    }


}