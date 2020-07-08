package com.goodchoice.android.ohneulen.ui.store.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMapFragmentBinding
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kotlinx.android.synthetic.main.store_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreMapFragment : Fragment() ,OnMapReadyCallback{

    private lateinit var binding: StoreMapFragmentBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private lateinit var mapView: com.naver.maps.map.MapView


    companion object {
        fun newInstance() =
            StoreMapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_map_fragment,
            container,
            false
        )
        mapView = binding.storeMapView
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        Timber.e(binding.storeMapView.height.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val x = storeViewModel.storeInfo[0].addressX
        val y = storeViewModel.storeInfo[0].addressY
        val cameraUpdate=CameraUpdate.scrollTo(LatLng(x,y))
        naverMap.moveCamera(cameraUpdate)
    }

}