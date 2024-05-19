package com.capstone.jachulsa.exception

import com.capstone.jachulsa.dto.ErrorResponse
import com.capstone.jachulsa.dto.ResponseCode
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    protected fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(e.responseCode)
    }

    //Validation 관련 예외 -@NOTEMPTY(공백)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val fieldErrors = e.bindingResult.fieldErrors
        val fieldError = fieldErrors[fieldErrors.size - 1]  // 가장 첫 번째 에러 필드
        val fieldName = fieldError.field   // 필드명
        val rejectedValue = fieldError.rejectedValue   // 입력값

        val responseCode = ResponseCode.NOT_NULL_FIELD
        responseCode.message = "$fieldName 필드의 입력값 [ $rejectedValue ]이 유효하지 않습니다." //@NotEmpty(message="")
        return ErrorResponse.toResponseEntity(responseCode)
    }

    //Validation 관련 예외 - null, 형식 오류
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val cause = e.cause as? MismatchedInputException
            ?: return ErrorResponse.toResponseEntity(ResponseCode.HTTP_NOT_READABLE)

        val mismatchedFieldNames = cause.path.mapNotNull { it.fieldName }
        if (mismatchedFieldNames.isEmpty()) {
            return ErrorResponse.toResponseEntity(ResponseCode.HTTP_NOT_READABLE)
        }

        val responseCode = ResponseCode.NOT_READABLE
        responseCode.message = "${mismatchedFieldNames.joinToString()} 필드가 누락되었거나 데이터 형식이 올바르지 않습니다."
        return ErrorResponse.toResponseEntity(responseCode)
    }

    //타입 mismatched
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        val responseCode = ResponseCode.TYPE_MISMATCHED
        responseCode.message = "${e.name} 필드의 입력값 [ ${e.value} ]의 형식이 올바르지 않습니다."
        return ErrorResponse.toResponseEntity(responseCode)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(ResponseCode.RESOURCE_NOT_FOUND)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(e:MissingRequestHeaderException): ResponseEntity<ErrorResponse>{
        return ErrorResponse.toResponseEntity(ResponseCode.MISSING_HEADER)
    }

    // 잘못된 요청 처리
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFoundException(e: NoHandlerFoundException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(ResponseCode.RESOURCE_NOT_FOUND)
    }
}