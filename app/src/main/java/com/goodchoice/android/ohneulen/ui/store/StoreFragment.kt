package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.ImageDetailStoreDialog
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.home.StoreHome
import com.goodchoice.android.ohneulen.ui.store.map.StoreMap
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenu
import com.goodchoice.android.ohneulen.ui.store.review.StoreReview
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.dpToPx
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.textColor
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreFragment : Fragment() {

    companion object {
        fun newInstance() = StoreFragment()
        var storeSeq: String = ""
        private var first = false

        var state = 0
    }

    private var check = false
    private lateinit var binding: StoreFragmentBinding
    private val storeViewModel: StoreViewModel by viewModel()


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

        //로그인했을때
        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            storeViewModel.getStoreDetail(storeSeq)
        })

        //데이터가 바뀔때마다
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
//            메뉴 없으면 메뉴탭 삭제
            replaceAppbarFragment(StoreAppBar.newInstance())
            if (it.menuList.isNullOrEmpty()) {
                viewPagerSettingNullMenu()
            } else {
                viewPagerSetting()
            }
            hashTagGenerate(it)
            storeImage(it)
            binding.storeNewScrollView.visibility = View.VISIBLE
            MainActivity.bottomNav.visibility = View.GONE
            if (first || check) {
                binding.storeNewScrollView.scrollTo(0, 1)
                binding.storeNewScrollView.smoothScrollTo(0, 0)
                if (MainActivity.supportFragmentManager.findFragmentByTag("loading") != null) {
                    (MainActivity.supportFragmentManager.findFragmentByTag("loading") as DialogFragment).dismiss()
                }
            }


            //평점세팅
            storePoint(it)
//            binding.storeFragmentViewPager2.adapter!!.notifyDataSetChanged()
            binding.storeFragmentViewPager2.currentItem = state

        })
        stickyHeader()

    }


    override fun onPause() {
        super.onPause()
    }

//    override fun onStart() {
//        super.onStart()
//        storeViewModel.getStoreDetail(storeSeq)
//    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
        state = 0
//        first = false
    }

    //이미지 세팅(1장 or 여러장)
    private fun storeImage(storeDetail: StoreDetail) {
        //노 이미지일때
        if (storeDetail.storeInfo.image.isEmpty()) {
            binding.storeFragmentOneImage.visibility = View.VISIBLE
            binding.storeFragmentImageRv.visibility = View.GONE
            Glide.with(requireContext()).load(R.drawable.store_home_no_img)
                .into(binding.storeFragmentOneImage)
        } else if (storeDetail.storeInfo.image.size == 1) {
            //이미지가 한개일때
            binding.storeFragmentOneImage.visibility = View.VISIBLE
            binding.storeFragmentImageRv.visibility = View.GONE
            Glide.with(binding.storeFragmentOneImage.context)
                .load("${BaseUrl.OHNEULEN}${storeDetail.storeInfo.image[0].photoURL}")
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20)))
                .into(binding.storeFragmentOneImage)
        } else {
            //여러개일때
            binding.storeFragmentOneImage.visibility = View.INVISIBLE
            binding.storeFragmentImageRv.visibility = View.VISIBLE
//            Glide.with(requireContext())
//                .load(R.drawable.store_home_no_img)
//                .apply {
//                    RequestOptions().transform(RoundedCorners(20))
//                }
//                .into(binding.storeFragmentOneImage)
//            binding.storeFragmentImageRv.s
        }
        storeHeader(storeDetail)

    }


    //카테고리 좋아요 후기 갯수
    private fun storeHeader(storeDetail: StoreDetail) {
        val store = storeDetail.storeInfo.storeFull

        val likeCnt =
            textColor(
                //좋아요 갯수 가져오기
                storeDetail.storeInfo.storeFull.likeCnt.toString(),
                0,
                storeDetail.storeInfo.storeFull.likeCnt.toString().length,
                //색깔설정
                ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
            )
        val reviewCnt = textColor(
            storeDetail.reviewCnt.toString(),
            0,
            storeDetail.reviewCnt.toString().length,
            ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
        )

        val text =
            TextUtils.concat("${store.cate1Name!!.minorName} / 좋아요 ", likeCnt, " / 후기 ", reviewCnt)
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
        binding.storeFragmentViewPager2.adapter = StorePagerAdapter(
            getFragmentList(), childFragmentManager,
            lifecycle
        )
        binding.storeFragmentViewPager2.offscreenPageLimit = getFragmentList().size


        //탭 연결
        val tabLayoutTextList = mutableListOf("홈", "지도", "메뉴", "후기")
        TabLayoutMediator(binding.storeTab, binding.storeFragmentViewPager2) { tab, position ->
            tab.text = tabLayoutTextList[position]
        }.attach()
//        Timber.e(binding.storeTab.tabCount.toString())
        binding.storeNewScrollView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.storeFragmentViewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
//                    binding.storeFragmentViewPager2.setCurrentItem(position, true)
                    state = position

                    val view =
                        (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                            position
                        )
                    view?.let {
                        if (position == 1) {
                            mapSetting()
                        } else {
                            //viewPager 크기조절
                            updatePagerHeightForChild(view, binding.storeFragmentViewPager2)

                        }
                    }
                }

            }
        )
    }

    private fun viewPagerSettingNullMenu() {
        binding.storeFragmentViewPager2.adapter = StorePagerAdapter(
            getFragmentListNullMenu(), childFragmentManager,
            lifecycle
        )
        binding.storeFragmentViewPager2.offscreenPageLimit = getFragmentList().size

        //탭 연결
        val tabLayoutTextList = mutableListOf("홈", "지도", "후기")
        TabLayoutMediator(binding.storeTab, binding.storeFragmentViewPager2) { tab, position ->
            tab.text = tabLayoutTextList[position]
        }.attach()
        binding.storeNewScrollView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        binding.storeFragmentViewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    state = position
                    val view =
                        (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                            position
                        )
                    view?.let {
                        if (position == 1) {
                            mapSetting()
                        } else {
                            if (position == 0) {
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorBackgroundF6
                                    )
                                )
                            } else {
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorWhite
                                    )
                                )
                            }
                            //viewPager 크기조절
                            updatePagerHeightForChild(view, binding.storeFragmentViewPager2)
                        }
                    }
//                    if(position==2){
//                        state=2
//                        (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).notifyItemChanged(
//                            2
//                        )
//                    }
                }


            }
        )
//        binding.storeFragmentViewPager2.currentItem = state
    }

    //viewPager2 크기조절
    //탭 스크롤마다 크기 다르게 주기
    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.topToBottom = R.id.store_fragment_border2
            pager.layoutParams = layoutParams
            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
            binding.storeNewScrollView.scrollTo(0, 1)
            binding.storeNewScrollView.smoothScrollTo(0, 0)
            if (MainActivity.supportFragmentManager.findFragmentByTag("loading") != null) {
                (MainActivity.supportFragmentManager.findFragmentByTag("loading") as DialogFragment).dismiss()
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

    private fun getFragmentListNullMenu(): ArrayList<Fragment> {
        return arrayListOf(
            StoreHome.newInstance(),
            StoreMap.newInstance(),
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
        binding.storeFragmentViewPager2.layoutParams = layoutParams
    }

    private fun hashTagGenerate(storeDetail: StoreDetail) {
        binding.storeFragmentHashTag.removeAllViews()
        for (i in storeDetail.hashtagList) {
            val tv = TextView(requireContext())
            tv.height = 25.dpToPx()
            tv.text = "#${i.keyword}"
            tv.gravity = Gravity.CENTER
            tv.setPadding(13.dpToPx(), 0, 13.dpToPx(), 0)
            tv.setTextColor(requireContext().getColor(R.color.colorOhneulen))
            tv.background =
                requireContext().getDrawable(R.drawable.background_border_rounding_ohneulen)
            binding.storeFragmentHashTag.addView(tv)
        }
    }

    private fun storePoint(storeDetail: StoreDetail) {
        var point = 0.0
        for (i in storeDetail.reviewList) {
            point += i.point_1.toDouble()
        }
        point /= storeDetail.reviewCnt
        binding.storeFragmentRating.text = ((point * 10).toInt() / 10.0).toString()
        binding.storeFragmentRatingBar.rating = ((point * 10).toInt() / 10.0).toFloat()
    }

    fun oneImageClick(view: View) {
        if (storeViewModel.storeDetail.value!!.storeInfo.image.isEmpty())
            return
        val dialog = ImageDetailStoreDialog.newInstance(0)
        dialog.show(MainActivity.supportFragmentManager, "")
    }


}