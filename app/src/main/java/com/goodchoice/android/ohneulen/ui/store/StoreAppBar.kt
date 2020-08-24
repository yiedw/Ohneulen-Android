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
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


class StoreAppBar : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance() = StoreAppBar()
    }

    private lateinit var binding: StoreAppbarBinding
    private lateinit var shareLink: String
    private val storeViewModel: StoreViewModel by viewModel()

    //나중에 되돌리기
//    private val initMainFragment: ViewGroup.LayoutParams =
//        MainActivity.mainFrameLayout.layoutParams

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
        val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
            //웹으로 봤을때 페이지
            link =
                Uri.parse("https://www.ohneulen.com/" + ConstList.SEGMENT_STORE + "?" + ConstList.SEQ + "=" + StoreFragment.storeSeq)
            domainUriPrefix = "https://ohneulen.page.link"
            androidParameters("com.goodchoice.android.ohneulen") { }
            iosParameters("com.goodchoice.ios.ohneulen") {}
//            buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
        }

        //하트상태 받아오기
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            binding.storeAppbarLike.isSelected = it.storeInfo.store.likes
        })

        //공유하기 링크
        dynamicLink.addOnSuccessListener {
            shareLink = dynamicLink.result!!.shortLink.toString()
        }
    }


    fun likeClick(view: View) {
        if (!LoginViewModel.isLogin.value!!) {
            binding.storeAppbarLike.isSelected = false
            loginDialog(requireContext(), newInstance())
            return
        }
        binding.storeAppbarLike.isSelected = !binding.storeAppbarLike.isSelected
        if (binding.storeAppbarLike.isSelected) {
            Toast.makeText(requireContext(), "찜 목록에 저장되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareClick(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sharing = Intent.createChooser(intent, "share")
        intent.putExtra(Intent.EXTRA_TEXT, shareLink)
        startActivity(sharing)
    }

    fun backClick(view: View) {
        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        if (MainActivity.supportFragmentManager.backStackEntryCount == 0) {
            replaceAppbarFragment(SearchAppBar.newInstance())
            replaceMainFragment(Search.newInstance())
        } else {
            replaceAppbarFragment(SearchAppBar.newInstance(true))
            MainActivity.supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        if (MainActivity.supportFragmentManager.backStackEntryCount == 0) {
            replaceAppbarFragment(SearchAppBar.newInstance())
            replaceMainFragment(Search.newInstance())
        } else {
            replaceAppbarFragment(SearchAppBar.newInstance(true))
            MainActivity.supportFragmentManager.popBackStack()
        }
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