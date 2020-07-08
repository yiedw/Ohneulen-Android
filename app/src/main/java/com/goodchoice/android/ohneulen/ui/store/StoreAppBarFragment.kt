package com.goodchoice.android.ohneulen.ui.store

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import timber.log.Timber


class StoreAppBarFragment : Fragment() {

    companion object {
        fun newInstance() = StoreAppBarFragment()
    }

    private lateinit var binding: StoreAppbarFragmentBinding

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
            R.layout.store_appbar_fragment,
            container,
            false
        )
        changeBlack()
        binding.fragment = this

        return binding.root
    }
    fun changeBlack(){
        binding.storeAppbarBack.setTextColor(Color.BLACK)
        binding.storeAppbarShare.setTextColor(Color.BLACK)
    }
    fun changeWhite(){
        binding.storeAppbarBack.setTextColor(Color.WHITE)
        binding.storeAppbarShare.setTextColor(Color.WHITE)
    }

    fun backClick(view: View) {
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        MainActivity.supportFragmentManager.popBackStack()
    }

    fun shareClick(view: View) {
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain"
//        val shareLink = "sample"
//        intent.putExtra(Intent.EXTRA_TEXT, shareLink)
//        val sharing = Intent.createChooser(intent, "공유하기")
//        startActivity(sharing)
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    "디저트 사진",
                    "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                    LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build()
                )
                    .setDescrption("아메리카노, 빵, 케익")
                    .build()
            )
            .setSocial(
                SocialObject.newBuilder()
                    .setLikeCount(10)
                    .setCommentCount(20)
                    .setSharedCount(30)
                    .setViewCount(40)
                    .build()
            )
            .addButton(
                ButtonObject(
                    "웹에서 보기",
                    LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build()
                )
            )
            .addButton(
                ButtonObject(
                    "앱에서 보기",
                    LinkObject.newBuilder()
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()
                )
            )
            .build()
        val serverCallbackArgs: MutableMap<String, String> = HashMap()
        serverCallbackArgs["user_id"]="\${current_user_id}"
        serverCallbackArgs["product_id"]="\${current_product_id}"

        KakaoLinkService.getInstance().sendDefault(
            requireContext(),
            params,
            serverCallbackArgs,
            object :ResponseCallback<KakaoLinkResponse>(){
                override fun onSuccess(result: KakaoLinkResponse?) {
//                    Timber.e("성공")
                }

                override fun onFailure(errorResult: ErrorResult?) {
                }

            }
        )

    }


}