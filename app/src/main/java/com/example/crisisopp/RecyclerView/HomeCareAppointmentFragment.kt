package com.example.crisisopp.RecyclerView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.example.crisisopp.adapters.HomeCareAppointmentAdapter
import com.example.crisisopp.home.viewmodel.HomeViewModel


class HomeCareAppointmentFragment: Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var homeCareAppointmentAdapter: HomeCareAppointmentAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_care_appointment, container, false)
        homeCareAppointmentAdapter = HomeCareAppointmentAdapter(homeViewModel)
        recyclerView = view.findViewById(R.id.recycler_view_home_care_appointments)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = homeCareAppointmentAdapter.apply {
                notifyDataSetChanged()
            }
            return view
        }
    }
    override fun onStart() {
        super.onStart()
        homeCareAppointmentAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        homeCareAppointmentAdapter.stopListening()
    }
}
