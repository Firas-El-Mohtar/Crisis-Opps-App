package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource
import com.example.crisisopp.user.UserTYPE

class HomeRepository(val homeDataSource: HomeDataSource, val userType: String) {

    fun floatingActionButtonStatus(userType: String){
        if (userType == "local"){

        }
    }
}