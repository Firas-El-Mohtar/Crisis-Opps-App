package com.example.crisisopp.extensions

var String.emailDomain: String
    set(value) = Unit
    get()= Regex("(?<=@)[^.]*.[^.]*(?=\\.)", RegexOption.IGNORE_CASE).find(this)?.value ?: ""


