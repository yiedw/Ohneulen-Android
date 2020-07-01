package com.goodchoice.android.ohneulen.ui.store

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMapFragmentBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreMapFragment : Fragment() {

    private lateinit var binding: StoreMapFragmentBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private lateinit var mapView:MapView


    companion object {
        fun newInstance() = StoreMapFragment()
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
        val x = storeViewModel.storeInfo[0].addressX
        val y = storeViewModel.storeInfo[0].addressY
        val mapPoint = MapPoint.mapPointWithGeoCoord(x, y)
        mapView = MapView(requireContext())
        mapView.setMapCenterPoint(mapPoint, false)
        mapView.setZoomLevel(2, false)
        val mapViewContainer: ViewGroup = binding.storeMapView
        mapViewContainer.addView(mapView)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}