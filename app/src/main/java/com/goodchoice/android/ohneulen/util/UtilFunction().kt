package com.goodchoice.android.ohneulen.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Network
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.security.MessageDigest

//카카오 지도 불러오는데 사용
@SuppressLint("PackageManagerGetSignatures")
fun getAppKeyHash(context: Context) {
    try {
        val info: PackageInfo =
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val something = String(Base64.encode(md.digest(), 0))
            Timber.e("kakao : " + something)
        }
    } catch (e: Exception) {
        Timber.e(e.toString())
    }
}

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
    fragmentTransaction.replace(R.id.appbar_frameLayout, fragment, tag).commit()
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
    fragmentTransaction.add(R.id.appbar_frameLayout, fragment).commit()
}


fun Int.px(): Int {
    val metrics = App.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), metrics)
        .toInt()
}

fun Int.dp(): Int {
    val metrics = App.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
        .toInt()
}


fun fcmToken(context: Context) {
//    FirebaseApp.initializeApp(context)
    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { task ->
        Timber.e("fcmToken : $task.token")
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

fun loginDialog(context: Context, backFragmentAppBar: Fragment) {
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
        replaceAppbarFragment(LoginAppBar.newInstance(true, backFragmentAppBar))
        addMainFragment(Login.newInstance(), true)
        dialog.dismiss()
    }
    dialog.show()
}

fun getOhneulenData(networkService:NetworkService,mainCode:String){
    CoroutineScope(Dispatchers.IO).launch {
        val mainCategoryResponse=
            networkService.requestOhneulenData(mainCode.toRequestBody())
    }
}

