package com.example.crisisopp.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.view.HomeActivity
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

/**
 * This dialog is presented to the user upon clicking the filter button in the appbar
 */

class FilterDialog : DialogFragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.filter_dialog, container, false)
        val approved = view.findViewById<SwitchMaterial>(R.id.approved_forms)
        val rejected = view.findViewById<SwitchMaterial>(R.id.rejected_forms)
        val requested = view.findViewById<SwitchMaterial>(R.id.requested_forms)
        val done = view.findViewById<SwitchMaterial>(R.id.done_forms)
        approved.setOnCheckedChangeListener {buttonView, isChecked ->
            buttonView.setOnClickListener {
                if (isChecked){
                    homeViewModel.setSelectedFilter("Approved")
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    dialog?.dismiss()
                }
            }
        }
        rejected.setOnCheckedChangeListener {buttonView, isChecked ->
            buttonView.setOnClickListener {
                if (isChecked){
                    homeViewModel.setSelectedFilter("Rejected")
                    dialog?.dismiss()
                }
            }
        }
        requested.setOnCheckedChangeListener {buttonView, isChecked ->
            buttonView.setOnClickListener {
                if (isChecked){
                    homeViewModel.setSelectedFilter("Requested")
                    dialog?.dismiss()
                }
            }
        }
        done.setOnCheckedChangeListener {buttonView, isChecked ->
            buttonView.setOnClickListener {
                if (isChecked){
                    homeViewModel.setSelectedFilter("Done")
                    dialog?.dismiss()
                }
            }
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }
}

