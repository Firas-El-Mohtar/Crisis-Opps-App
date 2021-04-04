package com.example.crisisopp.LocalMunicipality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.example.crisisopp.home.models.HomeCareForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.testingthings.history.FirestoreAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject


class HomeCareFormsAdapter(private val homeViewModel: HomeViewModel) : FirestoreAdapter<HomeCareFormsAdapter.HomeCareFormViewHolder>(homeViewModel.querySelector()!!) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCareFormViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_items, parent, false)
        return HomeCareFormViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: HomeCareFormViewHolder, position: Int) {
        return holder.bind(getSnapshot(position))
    }

    inner class HomeCareFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val _aynwzayn: ImageView = itemView.findViewById(R.id.ayn_wzayn_icon)
        val _mainapproval: ImageView = itemView.findViewById(R.id.markazeyye_icon)
        val _farahapproval: ImageView = itemView.findViewById(R.id.farah_foundation_icon)
        val personName: TextView = itemView.findViewById(R.id.full_name)
        val phoneNumber: TextView = itemView.findViewById(R.id.phone_number_holder)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(snapshot: DocumentSnapshot) {
            val form = snapshot.toObject<HomeCareForm>()
            personName.text = "Full Name: " + form?.fullName
            phoneNumber.text = "Phone Number: " + form?.phoneNumber

            when (form?.farahApproval) {
                -1 -> _farahapproval.setImageResource(R.drawable.rejected_icon)
                0 -> _farahapproval.setImageResource(R.drawable.pending_icon)
                1 -> _farahapproval.setImageResource(R.drawable.approved_icon)
            }

            when (form?.mainApproval) {
                -1 -> _mainapproval.setImageResource(R.drawable.rejected_icon)
                0 -> _mainapproval.setImageResource(R.drawable.pending_icon)
                1 -> _mainapproval.setImageResource(R.drawable.approved_icon)
            }

            when (form?.ainWzeinApproval) {
                -1 -> _aynwzayn.setImageResource(R.drawable.rejected_icon)
                0 -> _aynwzayn.setImageResource(R.drawable.pending_icon)
                1 -> _aynwzayn.setImageResource(R.drawable.approved_icon)
            }
        }
        override fun onClick(v: View?) {
            getSnapshot(adapterPosition).toObject<HomeCareForm>()?.let {
                homeViewModel.setSelectedHomeCareForm(it)
            }
            }
        }
}


