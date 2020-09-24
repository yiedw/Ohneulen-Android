package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
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
import com.goodchoice.android.ohneulen.util.setHeight
import com.goodchoice.android.ohneulen.util.textColor
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreFragment : Fragment() {

    companion object {
        fun newInstance() = StoreFragment()
        var storeSeq: String = ""
        private var first = true

        //최초에 한번만 높이를 측정 -> minheight를 수정하기 위해서 (화면에 빈공간 남는거 수정)
        private var viewPager2Height = 0
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
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer { it ->
//            메뉴 없으면 메뉴탭 삭제
            replaceAppbarFragment(StoreAppBar.newInstance())
            if (it.menuList.isNullOrEmpty()) {
                viewPagerSettingNullMenu()
            } else {
                viewPagerSetting()
            }
            hashTagGenerate(it)
            storeImage(it)
            MainActivity.bottomNav.visibility = View.GONE

            //평점세팅
            storePoint(it)


            if (storeViewModel.storeReviewHeightCheck) {
                storeViewModel.storeReviewHeightCheck = false
                val view =
                    (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                        binding.storeFragmentViewPager2.currentItem
                    )
                val viewHeight =
                    view?.findViewById<LinearLayout>(R.id.store_home)?.height
                Timber.e(viewHeight.toString()+"review")
            }

        })

    }

    override fun onResume() {
        super.onResume()
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
        storeViewModel.storeDetail = MutableLiveData()
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
            binding.storeFragmentOneImage.visibility = View.GONE
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
//    private fun stickyHeader() {
//        binding.storeTab.bringToFront()
//        binding.storeNewScrollView.run {
//            header = binding.storeTab
//            freeHeader()
//        }
//    }


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
                    state = position

                    //정확한 viewpager 높이를 구하기 위해 바뀔때마다 높이를 원래대로 세팅해줌
                    if (viewPager2Height != 0) {
                        binding.storeFragmentViewPager2.setHeight(viewPager2Height)
                    }

                    val view =
                        (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                            position
                        )
                    view?.let {
                        val layoutParams = AppBarLayout.LayoutParams(
                            AppBarLayout.LayoutParams.MATCH_PARENT,
                            AppBarLayout.LayoutParams.WRAP_CONTENT
                        )
                        when (position) {
                            0 -> {
                                //지도를 제외하고는 스크롤
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

                                //home 일때 배경색 회색으로 바꿔줌 ( 밑에 남는공간)
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorBackgroundF6
                                    )
                                )
                                //공간이 남으면 아래로 스크롤 못하게 세팅
                                it.post {

                                    val viewHeight =
                                        it.findViewById<LinearLayout>(R.id.store_home).height
                                    minHeightSetting(viewHeight, layoutParams)
                                }
                            }

                            1 -> {
                                //지도일때는 스크롤 고정
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL

                                //나머지는 흰색
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorWhite
                                    )
                                )
                            }

                            2 -> {
                                //지도를 제외하고는 스크롤
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

                                //나머지는 흰색
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorWhite
                                    )
                                )

                                //공간이 남으면 스크롤 못하게
                                it.post {
                                    val viewHeight =
                                        it.findViewById<RecyclerView>(R.id.store_menu_rv).height
                                    minHeightSetting(viewHeight, layoutParams)
                                }
                            }

                            else -> {
                                //지도를 제외하고는 스크롤
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

                                //나머지는 흰색
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorWhite
                                    )
                                )

                                //공간이 남으면 스크롤 못하게
                                it.post {
                                    val viewHeight =
                                        it.findViewById<RecyclerView>(R.id.store_review_rv).height
                                    minHeightSetting(viewHeight, layoutParams)
                                }
                            }
                        }

                        binding.storeCollapsing.layoutParams = layoutParams
                        if (MainActivity.supportFragmentManager.findFragmentByTag("loading") != null) {
                            (MainActivity.supportFragmentManager.findFragmentByTag("loading") as DialogFragment).dismiss()
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
//        if (binding.storeFragmentViewPager2.currentItem == 2) {
//            val handler = Handler(Looper.getMainLooper())
//            handler.post {
//                Timber.e("sadf")
//                val layoutParams = AppBarLayout.LayoutParams(
//                    AppBarLayout.LayoutParams.MATCH_PARENT,
//                    AppBarLayout.LayoutParams.WRAP_CONTENT
//                )
//                val view =
//                    (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
//                        2
//                    )
//                val viewHeight =
//                    view?.findViewById<RecyclerView>(R.id.store_review_rv)?.height
//                minHeightSetting(viewHeight!!, layoutParams)
//            }
//        }

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
                    //정확한 viewpager 높이를 구하기 위해 바뀔때마다 높이를 원래대로 세팅해줌
                    if (viewPager2Height != 0) {
                        binding.storeFragmentViewPager2.setHeight(viewPager2Height)

                    }

                    val view =
                        (binding.storeFragmentViewPager2.adapter as StorePagerAdapter).getViewAtPosition(
                            position
                        )
                    view?.let {
                        val layoutParams = AppBarLayout.LayoutParams(
                            AppBarLayout.LayoutParams.MATCH_PARENT,
                            AppBarLayout.LayoutParams.WRAP_CONTENT
                        )

                        when (position) {
                            0 -> {
                                //지도를 제외하고는 스크롤
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

                                //home 일때 배경색 회색으로 바꿔줌 ( 밑에 남는공간)
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorBackgroundF6
                                    )
                                )
                                //공간이 남으면 아래로 스크롤 못하게 세팅
                                it.post {
                                    val viewHeight =
                                        it.findViewById<LinearLayout>(R.id.store_home).height
                                    minHeightSetting(viewHeight, layoutParams)
                                }
                            }

                            1 -> {
                                //지도일때는 스크롤 고정
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL

                                //나머지는 흰색
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorWhite
                                    )
                                )
                            }

                            else -> {
                                //지도를 제외하고는 스크롤
                                layoutParams.scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

                                //나머지는 흰색
                                binding.storeNewScrollView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorWhite
                                    )
                                )

                                //공간이 남으면 스크롤 못하게
                                it.post {
                                    val viewHeight =
                                        it.findViewById<RecyclerView>(R.id.store_review_rv).height
                                    minHeightSetting(viewHeight, layoutParams)
                                }
                            }
                        }
                        binding.storeCollapsing.layoutParams = layoutParams
                        if (MainActivity.supportFragmentManager.findFragmentByTag("loading") != null) {
                            (MainActivity.supportFragmentManager.findFragmentByTag("loading") as DialogFragment).dismiss()
                        }
                    }
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
//            layoutParams.topToBottom = R.id.store_fragment_border2
            pager.layoutParams = layoutParams
            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
//            binding.storeNewScrollView.scrollTo(0, 1)
//            binding.storeNewScrollView.smoothScrollTo(0, 0)
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

    private fun minHeightSetting(viewHeight: Int, layoutParams: AppBarLayout.LayoutParams) {
        if (first) {
            viewPager2Height = binding.storeFragmentViewPager2.height
            first = false
        }
        var minHeight =
            40.dpToPx() + viewPager2Height - viewHeight
        Timber.e(minHeight.toString())
        Timber.e(viewPager2Height.toString() + "init")
        Timber.e(binding.storeFragmentViewPager2.height.toString())
        Timber.e(viewHeight.toString())
        if (minHeight < 40.dpToPx()) {
            minHeight = 40.dpToPx()
        }
        //스크롤 못하는경우 스크롤을 막아줌
        else if (minHeight > binding.storeAppbarLayout.height) {
            minHeight = 40.dpToPx()
            layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
        }
        binding.storeToolbar.minimumHeight = minHeight
    }

    private fun refresh(){
        binding.storeFragmentViewPager2.adapter?.notifyDataSetChanged()
    }

    fun oneImageClick(view: View) {
        if (storeViewModel.storeDetail.value!!.storeInfo.image.isEmpty())
            return
        val dialog = ImageDetailStoreDialog.newInstance(0)
        dialog.show(MainActivity.supportFragmentManager, "")
    }


}