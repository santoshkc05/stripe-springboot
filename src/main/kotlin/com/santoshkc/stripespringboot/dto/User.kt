package com.santoshkc.stripespringboot.dto

data class User(
    val firebaseId: String,
    val stripeCustomerId: String?,
    val email: String
)