package com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.auth

data class RegisterRequest(
    val address: String,
    val name: String,
    val password: String,
    val password_confirmation: String,
    val username: String
)