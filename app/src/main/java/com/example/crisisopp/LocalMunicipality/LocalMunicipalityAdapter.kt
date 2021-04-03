package com.example.crisisopp.LocalMunicipality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class LocalMunicipalityAdapter(query: Query) : FirestoreLocalMunicipalityAdapter<LocalMunicipalityAdapter.ViewHolder>(query) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val nameHolder: TextView = itemView.findViewById(R.id.full_name)
//        private val familyNameHolder: TextView = itemView.findViewById(R.id.family_name_holder)
        private val phoneNumberHolder: TextView = itemView.findViewById(R.id.phone_number_holder)
        private val markazeyeIcon: TextView = itemView.findViewById(R.id.markazeyye_icon)
        private val ainWZeinIcon: TextView = itemView.findViewById(R.id.ayn_wzayn_icon)
        private val farahIcon: TextView = itemView.findViewById(R.id.farah_foundation_icon)



        fun bind(snapshot: DocumentSnapshot) {

//            val form = snapshot.toObject<ParentForm>()
//
//            nameHolder.text = "Name: " + form?.fullName
//            phoneNumberHolder.text = form?.phoneNumber

        }
        init {  
            itemView.setOnClickListener { v: View-> 
                val position: Int = adapterPosition

//                Toast.makeText(, "", Toast.LENGTH_SHORT).show()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.RecyclerViewHolder, RecyclerViewFragment())
//                    .commitNow()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getSnapshot(position))
    }

    
}