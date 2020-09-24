package com.goodchoice.android.ohneulen.ui.store.map

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMapBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.dpToPx
import com.goodchoice.android.ohneulen.util.setHeight
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.maps.android.ui.IconGenerator
import org.koin.android.ext.android.inject
import timber.log.Timber

class StoreMap : Fragment(), OnMapReadyCallback {

    private lateinit var binding: StoreMapBinding
    private val storeViewModel: StoreViewModel by inject()
    private lateinit var mapView: MapView
    private lateinit var currentLatLng: LatLng


    companion object {
        fun newInstance() =
            StoreMap()
    }

    @SuppressLint("UseRequireInsteadOfGet")
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
        //화면에 딱맞게 크기를 조절
        //지도 높이를 구함
        //바텀네비게이션 y + 바텀네비게이션 height - 48dp(store_map_con height) - StoreFragment_AppbarLayout
        val height =
            MainActivity.bottomNav.y + MainActivity.bottomNav.height - 48.dpToPx() - (parentFragment as StoreFragment).view?.findViewById<AppBarLayout>(
                R.id.store_appbarLayout
            )!!.height
        binding.storeMap.post {
            binding.storeMap.setHeight(height.toInt())
        }

        mapView = binding.storeMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            currentLatLng =
                LatLng(
                    it.storeInfo.storeFull.addrY.toDouble(),
                    it.storeInfo.storeFull.addrX.toDouble()
                )
            binding.storeMapAddressRoad.text = it.storeInfo.storeFull.addrRoad1
            val addrOld =
                "${it.storeInfo.storeFull.addrDepth1} ${it.storeInfo.storeFull.addrDepth2} ${it.storeInfo.storeFull.addrDepth3}"
            binding.storeMapAddressOld.text = addrOld
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
        googleMap.uiSettings.setAllGesturesEnabled(false)
        //마커추가
        addMarker(googleMap, currentLatLng)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
    }

    private fun addMarker(googleMap: GoogleMap, latLng: LatLng) {
        val iconGenerator = IconGenerator(requireContext())
        val markerView =
            LayoutInflater.from(requireContext()).inflate(R.layout.store_map_marker, null)
        iconGenerator.setContentView(markerView)
        iconGenerator.setBackground(null)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
        googleMap.addMarker(markerOptions)
    }


    fun onNavClick(view: View) {
        //매장데이터 넣어주기
        try {
            val kakaoMap =
                "kakaomap://route?ep=${currentLatLng.latitude},${currentLatLng.longitude}&by=CAR"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoMap))
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "카카오맵이 깔려있지 않습니다 \n스토어로 이동합니다", Toast.LENGTH_SHORT)
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


}