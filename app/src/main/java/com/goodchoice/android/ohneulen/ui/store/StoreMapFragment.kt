package com.goodchoice.android.ohneulen.ui.store

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMapFragmentBinding
import net.daum.mf.map.api.MapView

class StoreMapFragment : Fragment() {

    private lateinit var binding: StoreMapFragmentBinding


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

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = MapView(context)
        mapView.setZoomLevel(2, false)
        val mapViewContainer: ViewGroup = binding.storeMapView
        mapViewContainer.addView(mapView)




    }
}