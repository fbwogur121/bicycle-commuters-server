package com.capstone.jachulsa.service

data class NaverCallback(
        val code: String,
        val state: String,
        val error: String?,
        val error_description: String?
)