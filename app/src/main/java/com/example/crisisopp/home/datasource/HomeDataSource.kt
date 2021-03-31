package com.example.crisisopp.home.datasource

import android.view.View
import com.example.crisisopp.home.models.Form
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeDataSource {

    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser

    fun master(usertype: String, mainFAB: FloatingActionButton) {
        if (usertype == "local"){
            displayActionButtonStatus(mainFAB)

        }else hideActionButton(mainFAB)

    }
    fun displayActionButtonStatus(mainFAB: FloatingActionButton){
        mainFAB.visibility = View.VISIBLE
    }
    fun hideActionButton(mainFAB: FloatingActionButton){
        mainFAB.visibility = View.GONE
    }

    fun getUserId(): String{
        return currentUser.uid
    }

    fun saveForm(form: Form){
        db.collection("forms").add(form)
    }
    fun getFormId():String{
        //Todo: query?
    }


    //Fun1: Does the logic for FAB  DONE
//    fun floatingActionButtonStatus(usertype){
//
//    }

    //Fun2: Decides what value to change in firebase based on userType

    //Fun3: Displays name on toolbar
}