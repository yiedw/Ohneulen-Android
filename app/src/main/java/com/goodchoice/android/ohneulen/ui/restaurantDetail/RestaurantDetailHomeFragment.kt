package com.goodchoice.android.ohneulen.ui.restaurantDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.RestaurantdetailHomeFragmentBinding
import kotlinx.android.synthetic.main.restaurantdetail_fragment.view.*

class RestaurantDetailHomeFragment : Fragment() {
    companion object {
        fun newInstance() =
            RestaurantDetailHomeFragment()
    }

    private lateinit var binding:RestaurantdetailHomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.restaurantdetail_home_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}