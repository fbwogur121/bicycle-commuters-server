package com.capstone.jachulsa.service

data class LoginRequest(
    val code: String,
    val state: String
)