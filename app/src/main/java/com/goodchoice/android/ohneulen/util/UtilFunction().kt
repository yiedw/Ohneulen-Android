package com.goodchoice.android.ohneulen.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.TypedValue
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import timber.log.Timber
import java.security.MessageDigest
import java.text.FieldPosition

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
            Timber.e(something)
        }
    } catch (e: Exception) {
        Timber.e(e.toString())
    }
}

fun replaceMainFragment(
    fragment: Fragment,
    bundle: Bundle? = null,
    addToBackStackBoolean: Boolean = false
) {
    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
    fragment.arguments = bundle
    if (addToBackStackBoolean)
        fragmentTransaction.addToBackStack(null)
    fragmentTransaction.replace(R.id.main_frameLayout, fragment).commitNow()
}

fun replaceAppbarFragment(
    fragment: Fragment,
    bundle: Bundle? = null,
    addToBackStackBoolean: Boolean = false
) {
    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
    fragment.arguments = bundle
    if (addToBackStackBoolean)
        fragmentTransaction.addToBackStack(null)
    fragmentTransaction.replace(R.id.appbar_frameLayout, fragment).commitNow()
}

fun Int.px(): Int {
    val metrics=App.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,this.toFloat(),metrics)
        .toInt()
}
fun Int.dp():Int{
    val metrics=App.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),metrics)
        .toInt()
}

fun subDataRefresh(position: Int){
    SearchViewModel.subCategory.value=SearchViewModel.subCategoryList[position]

}


