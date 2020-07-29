package com.goodchoice.android.ohneulen.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.TypedValue
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
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
    if(email.matches(emailRegex))
        return true
    return false
}



