package com.example.crisisopp.extensions

/**
 * These extensions are used to isolate parts of the user's email address in order to determine the user's identity and permissions
 */

var String.emailDomain: String
    set(value) = Unit
    get()= Regex("(?<=@)[^.]*.[^.]*(?=\\.)", RegexOption.IGNORE_CASE).find(this)?.value ?: ""

var String.municipalityName: String
    set(value) = Unit
    get()= Regex("([^@]+)", RegexOption.IGNORE_CASE).find(this)?.value ?: ""

