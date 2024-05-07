package com.capstone.jachulsa.dto

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val result: T?
) {
    companion object {
        fun <T> success(info: ResponseCode, result: T): ApiResponse<T> {
            return ApiResponse(info.code, info.message , result)
        }
    }
}
