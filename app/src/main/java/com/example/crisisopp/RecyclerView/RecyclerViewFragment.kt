package com.example.crisisopp.RecyclerView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.LocalMunicipality.FirestoreLocalMunicipalityAdapter
import com.example.crisisopp.LocalMunicipality.LocalMunicipalityAdapter
import com.example.crisisopp.R
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecyclerViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecyclerViewFragment() : Fragment() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var rVadapter: LocalMunicipalityAdapter
    val firestore = Firebase.firestore
//    val query = firestore.collection("forms").orderBy("formId", Query.Direction.DESCENDING).limit("50")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        rVadapter = LocalMunicipalityAdapter(query)
//        recyclerView = findViewById(R.id.fragment_recycler_view)
//        recyclerView.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            adapter = rVadapter.apply {
//                notifyDataSetChanged()
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecyclerViewFragment()
    }

}
