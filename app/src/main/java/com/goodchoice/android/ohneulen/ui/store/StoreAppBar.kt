package com.goodchoice.android.ohneulen.ui.store

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.like.LikeAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject


class StoreAppBar : Fragment(), OnBackPressedListener {
    // 1->search
    // 2->like

    companion object {
        fun newInstance() = StoreAppBar()
        var stat = 1
    }

    private lateinit var binding: StoreAppbarBinding

    //    private lateinit var shareLink: String
    private val storeViewModel: StoreViewModel by inject()
    private val searchViewModel: SearchViewModel by inject() //뒤로가기 했을때 리스트 새로고침

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_appbar,
            container,
            false
        )
//        changeBlack()
        binding.fragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //로그인했을때 찜상태 새로 받아오기
        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                storeViewModel.getStoreFavoriteCheck(StoreFragment.storeSeq)
            }
        })

        //찜상태 받아오기
        storeViewModel.storeFavoriteCheckLiveData.observe(viewLifecycleOwner, Observer {
            binding.storeAppbarLike.isSelected = it
            storeViewModel.storeFavoriteIsChecked = it

        })

        //하트눌렀을때
        storeViewModel.setMemberLikeResponseCode.observe(viewLifecycleOwner, Observer {
            if (it == ConstList.SUCCESS) {
                binding.storeAppbarLike.isSelected = !binding.storeAppbarLike.isSelected
                storeViewModel.storeFavoriteIsChecked = binding.storeAppbarLike.isSelected
                if (binding.storeAppbarLike.isSelected) {
                    Toast.makeText(requireContext(), "찜 목록에 저장되었습니다", Toast.LENGTH_SHORT).show()
                    //상단에 있는 좋아요 수 즉각반영
                    storeViewModel.storeLikeCnt = storeViewModel.storeLikeCntLiveData.value!! + 1
                    storeViewModel.storeLikeCntLiveData.postValue(storeViewModel.storeLikeCntLiveData.value!! + 1)
                } else {
                    //상단에 있는 좋아요 수 즉각반영
                    storeViewModel.storeLikeCnt = storeViewModel.storeLikeCntLiveData.value!! - 1
                    storeViewModel.storeLikeCntLiveData.postValue(storeViewModel.storeLikeCntLiveData.value!! - 1)
                }
                storeViewModel.setMemberLikeResponseCode.postValue("")
            } else if (it == ConstList.REQUIRE_LOGIN) {
                LoginViewModel.isLogin.postValue(false)
                binding.storeAppbarLike.isSelected = false
                loginDialog(requireContext(), false)
                storeViewModel.setMemberLikeResponseCode.postValue("")
            }

        })

        //공유하기 링크
    }

    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.refreshCheck.postValue(true)
        //찜여부
    }


    fun likeClick(view: View) {
        //로그인 되어있지 않을때
        if (!LoginViewModel.isLogin.value!!) {
            binding.storeAppbarLike.isSelected = false
            loginDialog(requireContext(), false)
            return
        }
        //로그인 되어있을때
        storeViewModel.setMemberLike()

    }

    fun shareClick(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sharing = Intent.createChooser(intent, "share")
        val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
            //웹으로 봤을때 페이지
            link =
                Uri.parse("https://www.ohneulen.com/store/view/${StoreFragment.storeSeq}")
            domainUriPrefix = "https://ohneulen.page.link"
//            Timber.e(requireActivity().packageName)
            androidParameters("com.goodchoice.android.ohneulen") { }
            iosParameters("com.goodchoice.ios.ohneulen") {}
//            buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
        }
        var shareLink = ""
        dynamicLink.addOnSuccessListener {
            shareLink = dynamicLink.result!!.shortLink.toString()
            intent.putExtra(Intent.EXTRA_TEXT, shareLink)
            startActivity(sharing)
        }
    }

    fun backClick(view: View) {
        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        if (MainActivity.supportFragmentManager.backStackEntryCount == 0) {
            MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
        } else {
            MainActivity.supportFragmentManager.popBackStack()
        }
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        if (MainActivity.supportFragmentManager.backStackEntryCount == 0) {
            MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
        } else {
            MainActivity.supportFragmentManager.popBackStack()
        }
        MainActivity.bottomNav.visibility = View.VISIBLE
    }


    //카카오링크
//        val params = FeedTemplate
//            .newBuilder(
//                ContentObject.newBuilder(
//                    "디저트 사진",
//                    "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
//                    LinkObject.newBuilder()
//                        .setWebUrl("https://developers.kakao.com")
//                        .setMobileWebUrl("https://developers.kakao.com")
//                        .build()
//                )
//                    .setDescrption("아메리카노, 빵, 케익")
//                    .build()
//            )
//            .setSocial(
//                SocialObject.newBuilder()
//                    .setLikeCount(10)
//                    .setCommentCount(20)
//                    .setSharedCount(30)
//                    .setViewCount(40)
//                    .build()
//            )
//            .addButton(
//                ButtonObject(
//                    "웹에서 보기",
//                    LinkObject.newBuilder()
//                        .setWebUrl("https://developers.kakao.com")
//                        .setMobileWebUrl("https://developers.kakao.com")
//                        .build()
//                )
//            )
//            .addButton(
//                ButtonObject(
//                    "앱에서 보기",
//                    LinkObject.newBuilder()
//                        .setAndroidExecutionParams("key1=value1")
//                        .setIosExecutionParams("key1=value1")
//                        .build()
//                )
//            )
//            .build()
//        val serverCallbackArgs: MutableMap<String, String> = HashMap()
//        serverCallbackArgs["user_id"] = "\${current_user_id}"
//        serverCallbackArgs["product_id"] = "\${current_product_id}"
//
//        KakaoLinkService.getInstance().sendDefault(
//            requireContext(),
//            params,
//            serverCallbackArgs,
//            object : ResponseCallback<KakaoLinkResponse>() {
//                override fun onSuccess(result: KakaoLinkResponse?) {
////                    Timber.e("성공")
//                }
//
//                override fun onFailure(errorResult: ErrorResult?) {
//                }
//
//            }
//        )


}