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


class HomecareAppointmentsAdapter(private val homeViewModel: HomeViewModel) :
    FirestoreAdapter<HomecareAppointmentsAdapter.HomeCareAppointmentViewHolder>(homeViewModel.homecareAppointmentQuerySelector()!!) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCareAppointmentViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.homecare_appointments_item, parent, false)
        return HomeCareAppointmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeCareAppointmentViewHolder, position: Int) {
        return holder.bind(getSnapshot(position))
    }

    inner class HomeCareAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date_tv = itemView.findViewById<TextView>(R.id.date_placeholder)
        val time_tv = itemView.findViewById<TextView>(R.id.time_placeholder)
        val location_tv = itemView.findViewById<TextView>(R.id.location_placeholder)

        fun bind(snapshot: DocumentSnapshot) {
            snapshot.toObject<HomecareAppointment>()?.let {
                date_tv.text = it.date
                time_tv.text = it.time
                location_tv.text = it.time
            }
        }
    }
}

