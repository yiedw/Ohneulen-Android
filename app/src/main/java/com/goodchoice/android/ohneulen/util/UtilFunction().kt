package com.goodchoice.android.ohneulen.util

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.text.DecimalFormat

//카카오 지도 불러오는데 사용
//@SuppressLint("PackageManagerGetSignatures")
//fun getAppKeyHash(context: Context) {
//    try {
//        val info: PackageInfo =
//            context.packageManager.getPackageInfo(
//                context.packageName,
//                PackageManager.GET_SIGNATURES
//            )
//        for (signature in info.signatures) {
//            val md: MessageDigest = MessageDigest.getInstance("SHA")
//            md.update(signature.toByteArray())
//            val something = String(Base64.encode(md.digest(), 0))
//            Timber.e("kakao : " + something)
//        }
//    } catch (e: Exception) {
//        Timber.e(e.toString())
//    }
//}

fun replaceMainFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = null
) {
    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
    if (addToBackStack)
        fragmentTransaction.addToBackStack("")
    if (tag != null) {
        fragmentTransaction.replace(R.id.main_frameLayout, fragment, tag).commit()
    } else
        fragmentTransaction.replace(R.id.main_frameLayout, fragment).commit()
}

fun replaceAppbarFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = null

) {
    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
    if (addToBackStack)
        fragmentTransaction.addToBackStack("")
}

fun addMainFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    name: String? = null
) {
    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
    if (addToBackStack) {
        if (name == null) {
            fragmentTransaction.addToBackStack("")
        } else {
            fragmentTransaction.addToBackStack(name)
        }

    }
    fragmentTransaction.add(R.id.main_frameLayout, fragment).commit()
}


fun addAppbarFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
    if (addToBackStack)
        fragmentTransaction.addToBackStack("")
}

fun popupFragment(fragment: Fragment, tag: String? = null) {
    MainActivity.supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.enter_bottom_to_top,
            R.anim.exit_top_to_bottom,
            R.anim.exit_top_to_bottom,
            R.anim.exit_top_to_bottom
        )
        .addToBackStack(null)
        .add(MainActivity.mainFrameLayout.id, fragment, tag)
        .commit()
}


fun Int.pxToDp(): Int {
    val metrics = App.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), metrics)
        .toInt()
}

fun Int.dpToPx(): Int {
    val metrics = App.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
        .toInt()
}


fun fcmToken(context: Context) {
//    FirebaseApp.initializeApp(context)
    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { task ->
//        Timber.e("fcmToken : $task.token")
    }

    //푸시알람 받기
    FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
            override fun onComplete(task: Task<InstanceIdResult>) {
                if (!task.isSuccessful) {
                    Timber.e("getInstanceId failed : ${task.exception}")
                    return
                }
            }

        })
}

//이메일 체크
fun emailCheck(email: String): Boolean {
    val emailRegex =
        Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
    if (email.matches(emailRegex))
        return true
    return false
}

//패스워드 체크
//영문,숫자로조합된 8자리 이상으로 입력
fun pwCheck(pw: String): Boolean {
    val pwRegex =
        Regex("^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$")
    if (pw.matches(pwRegex))
        return true
    return false
}


fun textColor(titleText: String, start: Int, end: Int, color: Int): SpannableString {
    val spannableString = SpannableString(titleText)
    spannableString.setSpan(
        ForegroundColorSpan(color),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

fun hideKeyboard(view: View, context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun loginDialog(context: Context, bottomNavVisibility: Boolean) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.logout_dialog)
    dialog.findViewById<TextView>(R.id.logout_dialog_tv2).text =
        context.getString(R.string.require_login)
    dialog.findViewById<TextView>(R.id.logout_dialog_tv1).text = "알림"
    dialog.findViewById<Button>(R.id.logout_dialog_cancel).setOnClickListener {
        dialog.dismiss()
    }

    dialog.findViewById<Button>(R.id.logout_dialog_ok).setOnClickListener {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.add(R.id.main_frameLayout, Login.newInstance(bottomNavVisibility))
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
        dialog.dismiss()
    }
    dialog.show()
}


suspend fun getOhneulenData(
    networkService: NetworkService,
    code: String
): MutableList<OhneulenData> {
    val list = mutableListOf<OhneulenData>()
    withContext(Dispatchers.IO) {
        try {
            val response =
                networkService.requestOhneulenData(code)
            for (i in response.resultData.indices) {
                val majorCode = response.resultData[i].majorCode
                val minorCode = response.resultData[i].minorCode
                val minorName = response.resultData[i].minorName
                list.add(OhneulenData(majorCode, minorCode, minorName, false))
            }

        } catch (e: Exception) {


        }
    }
    return list
}

suspend fun getOhneulenSubData(
    networkService: NetworkService,
    mainList: MutableList<OhneulenData>
): MutableList<List<OhneulenData>> {
    val subList = mutableListOf<List<OhneulenData>>()
    withContext(Dispatchers.IO) {
        try {
            for (i in mainList.indices) {
                val tempList = getOhneulenData(networkService, mainList[i].minorCode)
                subList.add(tempList)
            }

        } catch (e: Exception) {

        }
    }
    return subList
}

fun comma(number: Int): String {
    val formatter = DecimalFormat("###,###")
    return formatter.format(number)
}

//view Height Change
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun typefaceBold(context: Context): Typeface {
    return Typeface.create("sans-serif", Typeface.BOLD)
}





