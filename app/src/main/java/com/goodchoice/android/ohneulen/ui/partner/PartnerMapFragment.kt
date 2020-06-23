package com.goodchoice.android.ohneulen.ui.partner

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerMapFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.util.ConstList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.partner_map_fragment.*
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PartnerMapFragment : Fragment() {

    private lateinit var binding: PartnerMapFragmentBinding


    companion object {
        fun newInstance() = PartnerMapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.partner_map_fragment,
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
        val mapViewContainer: ViewGroup = binding.partnerMapView
        mapViewContainer.addView(mapView)




    }
}