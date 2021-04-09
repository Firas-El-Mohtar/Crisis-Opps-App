package com.example.crisisopp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.example.crisisopp.home.models.HomecareAppointment
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.testingthings.history.FirestoreAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class PcrAppointmentAdapter(private val homeViewModel: HomeViewModel) :
    FirestoreAdapter<PcrAppointmentAdapter.PcrAppointmentViewHolder>(homeViewModel.pcrAppointmentQuerySelector()!!) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PcrAppointmentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.pcr_appointments_item, parent, false)
        return PcrAppointmentViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: PcrAppointmentViewHolder, position: Int) {
        return holder.bind(getSnapshot(position))
    }
    inner class PcrAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date_tv = itemView.findViewById<TextView>(R.id.date_placeholder)
        val time_tv = itemView.findViewById<TextView>(R.id.time_placeholder)
        //        val location_tv = itemView.findViewById<TextView>(R.id.location_placeholder)
        fun bind(snapshot: DocumentSnapshot) {
            snapshot.toObject<HomecareAppointment>()?.let {
                date_tv.text = it.date
                time_tv.text = it.time
//                location_tv.text = it.time
            }
        }
    }
}
