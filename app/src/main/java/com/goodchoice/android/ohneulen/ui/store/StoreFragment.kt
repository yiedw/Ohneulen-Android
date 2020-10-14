package com.goodchoice.android.ohneulen.ui.store

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
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
import com.goodchoice.android.ohneulen.data.model.Cate1Name
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.ImageDetailStoreDialog
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Handler().postDelayed(200) {
            val animation = AlphaAnimation(0f, 1f)
            MainActivity.bottomNav.visibility = View.GONE
            MainActivity.bottomNav.animation = animation
        }
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

        appbarShadowRemove()    //앱바가 고정되면 나오는 쉐도우 삭제

        //데이터가 바뀔때마다
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer { it ->
            if (!storeViewModel.storeReviewHeightCheck) {
                //옵저버 중복 방지
//            메뉴 없으면 메뉴탭 삭제
                if (it.menuList.isNullOrEmpty()) {
                    viewPagerSettingNullMenu()
                } else {
                    viewPagerSetting()
                }
            } else {
                storeViewModel.storeReviewHeightCheck = false
                val index = binding.storeFragmentViewPager2.currentItem
                //리사이클러뷰가 정확히 언제 끝나는 지 알 수 없기때문에 딜레이를 줌
                //화면에는 영향 x
                Handler().postDelayed({
                    binding.storeFragmentViewPager2.currentItem = 0
                    binding.storeFragmentViewPager2.currentItem = index

                }, 300)
            }
            hashTagGenerate(it)
            storeImage(it)
//            MainActivity.bottomNav.visibility = View.GONE

            //평점세팅
            storeViewModel.storeLikeCntLiveData.postValue(it.storeInfo.storeFull.likeCnt)
            storePoint(it)

            //찜 여부 확인
            storeViewModel.storeFavoriteCheckLiveData.postValue(it.storeInfo.storeFull.likes)

            //리뷰 갯수 세팅
            storeViewModel.storeReviewCnt = it.reviewCnt


        })

        //좋아요 눌렀을때 상단에 있는 좋아요 수 즉각 반영
        storeViewModel.storeLikeCntLiveData.observe(viewLifecycleOwner, Observer {
            if (storeViewModel.storeDetail.value != null) {
                val storeDetail = storeViewModel.storeDetail.value!!
                storeHeader(
                    storeDetail.storeInfo.storeFull.cate1Name!!,
                    it,
                    storeDetail.reviewCnt
                )
            }

        })
    }

    override fun onResume() {
        super.onResume()
//        Handler().postDelayed(2000) {
//            MainActivity.bottomNav.visibility = View.GONE
//        }
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
//        MainActivity.bottomNav.visibility = View.VISIBLE
        state = 0
        storeViewModel.storeDetail = MutableLiveData()

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
        val store = storeDetail.storeInfo.storeFull
//        storeHeader(store.cate1Name!!, store.likeCnt, storeDetail.reviewCnt)

    }


    //카테고리 좋아요 후기 갯수
    private fun storeHeader(cate1Name: Cate1Name, likeCnt: Int, reviewCnt: Int) {
        storeViewModel.storeLikeCntLiveData.postValue(likeCnt)//좋아요 수 저장
        storeViewModel.storeLikeCnt = likeCnt
        storeViewModel.storeReviewCnt = reviewCnt //리뷰 수 저장
        //1000개이상이면 999+로 표시
        val mLikeCnt = if (likeCnt > 999) {
            "999+"
        } else if (likeCnt > 100) {
            "100+"
        } else {
            likeCnt.toString()
        }
        //좋아요 갯수에 색 입히기
        val colorLikeCnt =
            textColor(
                //좋아요 갯수 가져오기
                mLikeCnt,
                0,
                mLikeCnt.length,
                //색깔설정
                ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
            )

        val mReviewCnt = if (reviewCnt > 999) {
            "999+"
        } else if (reviewCnt > 100) {
            "100+"
        } else {
            reviewCnt.toString()
        }
        val colorReviewCnt = textColor(
            mReviewCnt,
            0,
            mReviewCnt.length,
            ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
        )

        val text =
            TextUtils.concat(
                "${cate1Name.majorName}·${cate1Name.minorName} / 좋아요 ",
                colorLikeCnt,
                " / 후기 ",
                colorReviewCnt
            )
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
                            binding.storeNewScrollView.visibility = View.VISIBLE
//                            MainActivity.supportFragmentManager.beginTransaction().show(this@StoreFragment)
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
                            binding.storeNewScrollView.visibility = View.VISIBLE
//                            MainActivity.supportFragmentManager.beginTransaction().show(this@StoreFragment)
                        }
                    }
                }


            }
        )
//        binding.storeFragmentViewPager2.currentItem = state
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


    //해시태그뷰 생성
    private fun hashTagGenerate(storeDetail: StoreDetail) {
        binding.storeFragmentHashTag.removeAllViews()
        if (storeDetail.hashtagList.isNullOrEmpty()) {
            //해시태그 없으면 뷰 숨기기
            binding.storeFragmentHashTag.visibility = View.GONE
            return
        }
        //있으면 뷰 보여주기
        binding.storeFragmentHashTag.visibility = View.VISIBLE
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

    //전체평점 계산
    private fun storePoint(storeDetail: StoreDetail) {
        var point = 0.0
        for (i in storeDetail.reviewList) {
            point += i.point_1.toDouble()
        }
        point /= storeDetail.reviewCnt
        binding.storeFragmentRating.text = ((point * 10).toInt() / 10.0).toString()
        binding.storeFragmentRatingBar.rating = ((point * 10).toInt() / 10.0).toFloat()

        //평점 보존
        storeViewModel.storePoint = binding.storeFragmentRating.text.toString().toDouble()

    }

    private fun minHeightSetting(viewHeight: Int, layoutParams: AppBarLayout.LayoutParams) {
        if (first) {
            viewPager2Height = binding.storeFragmentViewPager2.height
            first = false
        }
        var minHeight =
            40.dpToPx() + viewPager2Height - viewHeight
//        Timber.e(minHeight.toString())
//        Timber.e(viewPager2Height.toString() + "init")
//        Timber.e(binding.storeFragmentViewPager2.height.toString())
//        Timber.e(viewHeight.toString())
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

    private fun appbarShadowRemove() {
        val stateListAnimator =
            StateListAnimator()
        stateListAnimator.addState(
            IntArray(0),
            ObjectAnimator.ofFloat(binding.storeAppbarLayout, "elevation", 0f)
        )
        binding.storeAppbarLayout.stateListAnimator = stateListAnimator
    }


    fun oneImageClick(view: View) {
        if (storeViewModel.storeDetail.value!!.storeInfo.image.isEmpty())
            return
        val dialog = ImageDetailStoreDialog.newInstance(0)
        dialog.show(MainActivity.supportFragmentManager, "")
    }


}