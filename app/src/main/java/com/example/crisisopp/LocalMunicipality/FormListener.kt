package com.example.crisisopp.LocalMunicipality

import com.example.crisisopp.home.models.Form

class FormListener (val clickListener: (formId: String) -> Unit) {
    fun onClick(form: Form) = clickListener(form.formID)
}