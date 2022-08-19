package com.santoshkc.stripespringboot.dto

data class IntentResponse(
    val secret: String,
    val ephemeralKey: String,
    val customer: String
)