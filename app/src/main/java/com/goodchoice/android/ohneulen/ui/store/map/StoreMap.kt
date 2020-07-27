package com.goodchoice.android.ohneulen.ui.store.map

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMapBinding
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreMap : Fragment(), OnMapReadyCallback {

    private lateinit var binding: StoreMapBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private lateinit var mapView: com.naver.maps.map.MapView


    companion object {
        fun newInstance() =
            StoreMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_map,
            container,
            false
        )
        mapView = binding.storeMapView

        val layoutParams = ConstraintLayout.LayoutParams(
            0,
            0
        )
//
        layoutParams.bottomToTop = R.id.store_map_con
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID

        mapView.layoutParams = layoutParams
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        binding.fragment = this
        binding.store = storeViewModel.storeInfo
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun onNavClick(view: View) {
        //매장데이터 넣어주기
        try {
            val kakaoMap =
                "kakaomap://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=CAR"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoMap))
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "카카오맵이 깔려있지 않습니다 스토어로 이동합니다", Toast.LENGTH_SHORT)
                .show()
            val uri = "market://details?id=net.daum.android.map"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(naverMap: NaverMap) {
        val x = storeViewModel.storeInfo.addressX
        val y = storeViewModel.storeInfo.addressY
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(x, y))
        naverMap.moveCamera(cameraUpdate)
    }

}