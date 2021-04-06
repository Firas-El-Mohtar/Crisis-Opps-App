
package com.example.crisisopp.viewpager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.crisisopp.RecyclerView.HomeCareFormsFragment
import com.example.crisisopp.RecyclerView.PcrFormsFragment
import com.google.android.material.tabs.TabLayout

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :FragmentStateAdapter(fm, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeCareFormsFragment()
            1-> PcrFormsFragment()
            else -> Fragment()
        }
    }
}
