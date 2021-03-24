package com.example.crisisopp.LocalMunicipality

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.R
import com.example.crisisopp.LocalMunicipality.FirestoreLocalMunicipalityAdapter
import com.example.crisisopp.RecyclerView.RecyclerViewFragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class LocalMunicipalityAdapter(query: Query) : FirestoreLocalMunicipalityAdapter<LocalMunicipalityAdapter.ViewHolder>(query) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(snapshot: DocumentSnapshot) {

            //val match = snapshot.toObject<MatchHistory>()

            //name.text = "Player Name: " + match?.playerName
            //isWinner.text = "Winner: " + match?.isWinner
            //matchDuration.text = "Match Duration: " + match?.duration?.toFormattedTimestamp()
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