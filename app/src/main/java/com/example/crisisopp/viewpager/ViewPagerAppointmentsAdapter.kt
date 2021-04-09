package com.example.crisisopp.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.crisisopp.RecyclerView.HomeCareAppointmentFragment
import com.example.crisisopp.RecyclerView.HomeCareFormsFragment
import com.example.crisisopp.RecyclerView.PcrAppointmentFragment
import com.example.crisisopp.RecyclerView.PcrFormsFragment

class ViewPagerAppointmentsAdapter (fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeCareAppointmentFragment()
            1-> PcrAppointmentFragment()
            else -> Fragment()
        }
    }
}
