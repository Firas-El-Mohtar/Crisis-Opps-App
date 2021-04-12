package com.example.crisisopp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.testingthings.history.FirestoreAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject


class PcrFormAdapter(private val homeViewModel: HomeViewModel) :
    FirestoreAdapter<PcrFormAdapter.PcrFormViewHolder>(homeViewModel.pcrQuerySelector()!!) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PcrFormViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pcr_recycler_view_items, parent, false)
        return PcrFormViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PcrFormViewHolder, position: Int) {
        return holder.bind(getSnapshot(position))
    }

    inner class PcrFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val personName: TextView = itemView.findViewById(R.id.full_name_pcr_tv)
        val placeOfResidency: TextView = itemView.findViewById(R.id.place_of_residency_pcr)
        val phoneNumber: TextView = itemView.findViewById(R.id.phone_number_pcr)
        val dateOfinfection: TextView = itemView.findViewById(R.id.presumed_date_pcr)
        val approvedState: TextView = itemView.findViewById(R.id.approved_state_pcr)
        val requestedState: TextView = itemView.findViewById(R.id.requested_state_pcr)
        val rejectedState: TextView = itemView.findViewById(R.id.rejected_state_pcr)

        init {

            itemView.setOnClickListener(this)
        }

        fun bind(snapshot: DocumentSnapshot) {
            snapshot.toObject<PcrForm>()?.let {
                val form = it
                personName.text = form.fullName
                placeOfResidency.text = form.placeOfResidence
                phoneNumber.text = form.phoneNumber.toString()
                dateOfinfection.text = form.dateOfInfection
                if (it.ainWzeinApproval == 0) {
                    requestedState.visibility = View.VISIBLE
                    rejectedState.visibility = View.GONE
                    approvedState.visibility = View.GONE
                } else if (it.ainWzeinApproval == -1) {
                    rejectedState.visibility = View.VISIBLE
                    approvedState.visibility = View.GONE
                    requestedState.visibility = View.GONE
                } else if (it.ainWzeinApproval == 1) {
                    approvedState.visibility = View.VISIBLE
                    rejectedState.visibility = View.GONE
                    requestedState.visibility = View.GONE
                }
            }
        }

        override fun onClick(v: View?) {
            getSnapshot(adapterPosition).toObject<PcrForm>()?.let {
                homeViewModel.setSelectedPcrForm(it)
            }
        }
    }
}