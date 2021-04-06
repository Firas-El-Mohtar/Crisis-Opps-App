
package com.example.crisisopp.RecyclerView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.example.crisisopp.adapters.HomeCareFormsAdapter
import com.example.crisisopp.home.viewmodel.HomeViewModel
class HomeCareFormsFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var homeCareFormsAdapter: HomeCareFormsAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_care_forms, container, false)
        homeCareFormsAdapter = HomeCareFormsAdapter(homeViewModel)
        recyclerView = view.findViewById(R.id.recycler_view_test_1)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = homeCareFormsAdapter.apply {
                notifyDataSetChanged()
            }
            return view
        }
    }
    override fun onStart() {
        super.onStart()
        homeCareFormsAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        homeCareFormsAdapter.stopListening()
    }
}

