package com.capstone.jachulsa.dto

import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val code: Int,
    val message: String,

    ) {
    companion object {
        fun toResponseEntity(e: ResponseCode): ResponseEntity<ErrorResponse> { //responseCode를 받아서 코드와 메시지 추출
            return ResponseEntity
                .status(e.httpStatus)
                .body(
                    ErrorResponse(
                        //status = e.httpStatus.value(),
                        code = e.code,
                        message = e.message
                    )
                )
        }
    }
}
