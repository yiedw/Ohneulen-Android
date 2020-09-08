package com.goodchoice.android.ohneulen.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

//자주찾는 질문
@Keep
data class FAQ(
    @SerializedName("seq")
    val seq: String,
    @SerializedName("classification")
    val classification: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("check")
    var check: Boolean = false
)

fun getFAQ(): List<FAQ> {
    val FAQlist = mutableListOf<FAQ>()
    FAQlist.add(
        FAQ(
            "0", "[계정]", "비밀번호 변경은 어떻게 하나요?", "\"비밀번호는 [회원정보]에서 변경하실 수 있습니다.\n" +
                    "비밀번호 설정 시 특수문자를 사용하고 유추 가능한 반복적인 문자 사용을 줄이시면 " +
                    "보다 안전하게 계정을 보호하실 수 있습니다.\""
        )

    )
    FAQlist.add(
        FAQ(
            "0",
            "[계정]",
            "회원 탈퇴 시 작성한 리뷰가 삭제되나요?",
            "\"회원 탈퇴 시 회원관련 정보는 삭제되지만 이전에 등록한 리뷰는 삭제되지 않습니다.\n" +
                    "대신 리뷰에 있는 회원님의 개인정보 (이름,닉네임,이미지 등)는 보이지 않게됩니다.\""
        )

    )
    FAQlist.add(
        FAQ(
            "0",
            "[계정]",
            "회원 탈퇴 후 기존 사용하던 아이디로 재가입 하려하니 이미 사용중이라고 합니다.",
            "\"탈퇴 후에는 탈퇴 전 아이디를 재사용하여 가입하실 수 있습니다.\n" +
                    "\n" +
                    "단, 다른 회원님이 이미 해당 아이디를 사용중인 경우 해당 아이디로 가입하실 수 없습니다.\""
        )

    )
    FAQlist.add(
        FAQ(
            "0",
            "[계정]",
            "회원 탈퇴는 어떻게 하나요?",
            "회원 탈퇴는 [우측상단의 회원이름 클릭 > 회원정보 > 회원탈퇴 클릭]으로 진행하실 수 있습니다."
        )

    )
    FAQlist.add(
        FAQ(
            "0", "[계정]", "아이디/비밀번호를 잃어버렸습니다.", "[로그인 > 아이디/비밀번호 찾기]에서 아이디 또는 비밀번호를 찾으실 수 있습니다."
        )

    )
    FAQlist.add(
        FAQ(
            "0", "[매장]", "매장 정보 수정 요청했는데 왜 수정되지 않았나요?", "\"매장 정보 오류로 인한 수정 요청 시 바로 변경되기 보다는 " +
                    "해당 매장 사장님의 확인 후 처리가 됩니다.\n" +
                    "처리 과정에 따라 시간이 소요될 수 있습니다.\""
        )

    )
    FAQlist.add(
        FAQ(
            "0",
            "[매장]",
            "후기 작성은 어떻게 하나요?",
            "후기를 쓰려는 매장 정보로 가셔서 [후기 탭 > 후기 작성 버튼]을 통해 작성하실 수 있습니다."
        )

    )
    FAQlist.add(
        FAQ(
            "0", "[매장]", "후기 수정/삭제는 어떻게 하나요?", "후기를 작성한 계정으로 로그인한 후 후기를 수정/삭제하실 수 있습니다."
        )

    )
    FAQlist.add(
        FAQ(
            "0", "[매장]", "불쾌한 후기를 신고하고 싶습니다.", "\"해당 후기에는 신고 버튼이 있습니다.\n" +
                    "사유 선택과 함께 구체적인 내용을 작성해주시면 빠르게 처리해드리겠습니다.\""
        )

    )
    FAQlist.add(
        FAQ(
            "0", "[매장]", "작성한 후기가 보이지 않습니다.", "\"작성하신 후기가 관련 법령에 의거하여 모니터링되어 차단되었을 가능성이 큽니다.\n" +
                    "만일 이에 해당되지 않으신다면 고객센터로 문의 주시면 빠르게 확인 및 처리해드리겠습니다.\""
        )

    )
    FAQlist.add(
        FAQ(
            "0", "[기타]", "매장을 등록하고 싶습니다.", "\"매장의 사장님일 경우 고객센터로 문의 주시면 확인 및 등록 진행해드리겠습니다.\n" +
                    "\n" +
                    "고객님이 매장정보가 없어서 불편하신 경우 1:1문의에 남겨주시면 빠른 확인 및 처리해드리겠습니다.\""
        )

    )
    return FAQlist
}